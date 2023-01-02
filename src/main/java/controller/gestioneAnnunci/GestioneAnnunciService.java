package controller.gestioneAnnunci;

import java.util.List;

import model.Annuncio;
import model.Utente;

public interface GestioneAnnunciService {
	
	public List<Annuncio> getJobs();
	public List<Annuncio> getJobsAvailable();
	
	List<Annuncio> getCommissioni();
	
	List<Annuncio> getCommissioniDisponibili();
	
	//Insert annuncio into db
	void insertAnnuncio(Annuncio annuncio);
	
	//Remove annuncio into db
	void removeAnnuncio(Long annuncio);
}