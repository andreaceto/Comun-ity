package model;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import controller.utility.DbConnection;

public class AnnuncioDAO {

	//Connessione al database
	MongoDatabase database = DbConnection.connectToDb();


	//Inserisce un annuncio nel database
	public void saveAnnuncio(Annuncio annuncio) {

		try {
			annuncio.setId(lastIdAnnuncio()+1);
			database.getCollection("annuncio").insertOne(docForDb(annuncio));
			System.out.println("Annuncio aggiunto al database con successo");

		}catch(MongoException e) {
			System.out.println("Errore durante l'inserimento dell'annuncio" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	//this method returns the id of the last annuncio
	public Long lastIdAnnuncio() {
		Document myDoc = (Document)database.getCollection("annuncio").find().sort(new Document("id",-1)).first();
		Annuncio lastAnnuncio=docToAnnuncio(myDoc);
		return lastAnnuncio.getId();
	}

	//Esegue l'eliminazione di un annuncio nel database
	public void deleteAnnuncio(Long id) {
		try {

			database.getCollection("annuncio").deleteOne(Filters.eq("id", id));
			System.out.println("Annuncio eliminato!");
		}catch(MongoException e) {
			System.out.println("Errore durante l'eliminazione dell'annuncio" + e.getMessage());
		}
	}

	//Trova un annuncio specifico nel database
	public Annuncio findAnnuncioById(Long id) {

		Document doc=null;

		try {

			doc= database.getCollection("utente").find(Filters.eq("id", id)).first();
		}catch(MongoException e) {
			System.out.println("Errore durante la ricerca dell'annuncio" + e.getMessage());
		}

		Annuncio annuncio = docToAnnuncio(doc);
		return annuncio;

	}
	
	//Restituisce una lista con tutti i lavori 
	public List<Annuncio> findLavori(){
		
		List<Annuncio> lavori = new ArrayList<Annuncio>();
		
		try {
			
			MongoCursor<Document> cursor = database.getCollection("annuncio").find(Filters.ne("abilitazioneRichiesta", "nessuna")).iterator(); 
			
			while (cursor.hasNext()) {
				lavori.add(docToAnnuncio(cursor.next()));
			}
			

		}catch(MongoException e) {
			System.out.println("Errore durante la ricerca dei lavori disponibili");
		}
		
		return lavori;
	}
	
	//Restituisce una lista con tutte le commissioni
		public List<Annuncio> findCommissioni(){
			
			List<Annuncio> commissioni = new ArrayList<Annuncio>();
			
			try {
				
				MongoCursor<Document> cursor = database.getCollection("annuncio").find(Filters.eq("abilitazioneRichiesta", "nessuna")).iterator(); 
				if(!cursor.hasNext()) {
					return null;
				}
				while (cursor.hasNext()) {
					commissioni.add(docToAnnuncio(cursor.next()));
				}
				

			}catch(MongoException e) {
				System.out.println("Errore durante la ricerca delle commissioni disponibili");
			}
			
			return commissioni;
		}
	
	//Restituisce una lista di tutti i lavori disponibili
	public List<Annuncio> findLavoriDisponibili(){
		
		List<Annuncio> lavori = new ArrayList<Annuncio>();
		List<Annuncio> lavoriDisponibili = new ArrayList<Annuncio>();
		
		lavori= findLavori();
		
		Iterator<Annuncio> it= lavori.iterator();
		while(it.hasNext()) {
			if(it.next().getIncaricato().equals("nessuno")) {
				lavoriDisponibili.add(it.next());
			}
		}
		
		if(lavoriDisponibili.isEmpty()) {
			System.out.println("Non ci sono lavori disponibili");
		}
		
		return lavoriDisponibili;
		
	}
	
	//Restituisce una lista di tutte le commissioni disponibili
		public List<Annuncio> findCommissioniDisponibili(){
			
			List<Annuncio> commissioni = new ArrayList<Annuncio>();
			List<Annuncio> commissioniDisponibili = new ArrayList<Annuncio>();
			
			commissioni= findCommissioni();
			
			if(commissioni==null) {
				return null;
			}
			Iterator<Annuncio> it= commissioni.iterator();
			while(it.hasNext()) {
				if(it.next().getIncaricato().equals("nessuno")) {
					commissioniDisponibili.add(it.next());
				}
			}
			
			if(commissioniDisponibili.isEmpty()) {
				return null;
			}
			
			return commissioniDisponibili;
			
		}
		
	//Crea un documento per mongoDB
	private static Document docForDb(Annuncio annuncio) {

		Document doc= new Document("_id", new ObjectId())
				.append("id", annuncio.getId())
				.append("abilitazioneRichiesta", annuncio.getAbilitazioneRichiesta())
				.append("autore" , annuncio.getAutore())
				.append("titolo", annuncio.getTitolo())
				.append("descrizione" , annuncio.getDescrizione())
				.append("indirizzo", annuncio.getIndirizzo())
				.append("dataPubblicazione", annuncio.getDataPubblicazione())
				.append("incaricato", annuncio.getIncaricato())
				.append("dataFine", annuncio.getDataFine());

		return doc;

	}
	
	//Crea un istanza di Annuncio da un documento mongoDB
	private static Annuncio docToAnnuncio(Document doc) {

		Annuncio annuncio = new Annuncio(
				doc.getLong("id"),
				doc.getString("abilitazioneRichiesta"),
				doc.getString("autore"),
				doc.getString("titolo"),
				doc.getString("descrizione"),
				doc.getString("indirizzo"),
				(LocalDate)doc.getDate("dataPubblicazione").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
				doc.getString("incaricato"),
				(LocalDate)doc.getDate("dataFine").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				
		return annuncio;
	}

}