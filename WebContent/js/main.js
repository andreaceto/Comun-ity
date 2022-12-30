$(document).ready(function () {

    $('#dashboardSection').show();

	const showNavbar = (toggleId, navId, bodyId, headerId) => {
          const toggle = $('#'+toggleId)[0],
            nav = $('#'+navId)[0],
            bodypd = $('#'+bodyId)[0],
            headerpd = $('#'+headerId)[0];

          // Validate that all variables exist
          if (toggle && nav && bodypd && headerpd) {
            
            toggle.addEventListener("click", () => {
              // show navbar
              nav.classList.toggle("show");
              // change icon
              toggle.classList.toggle("bx-x");
              // add padding to body
              bodypd.classList.toggle("body-pd");
              // add padding to header
              headerpd.classList.toggle("body-pd");
            });
          }
        };
        
        showNavbar("header-toggle", "nav-bar", "body-pd", "header");

        /*===== LINK ACTIVE =====*/
        const linkColor = document.querySelectorAll(".nav_link");
        
        function colorLink() {
          if (linkColor) {
            linkColor.forEach((l) => l.classList.remove("active"));
            $('section').hide();
            this.classList.add("active");	
            var section = $(this).attr('id');
            $('#'+section+"Section").show();
          }
        }
        
        linkColor.forEach((l) => l.addEventListener("click", colorLink));
	
});