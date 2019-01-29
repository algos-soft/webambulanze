class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

//        "/"(view:"/index")
        "/$siglaCroce**"(controller: 'Accesso', action: 'seleziona')
        "/help"(controller: 'Accesso', action: 'help')
        "500"(view:'/error')
    } // fine della closure
} // fine della classe
