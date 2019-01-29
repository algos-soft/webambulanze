package webambulanze

class ViaggioService {

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def militeService

    //--lista dei militi abilitati alla funzione
    public ArrayList listaMilitiAbilitati(Croce croce, Funzione funzione) {
        ArrayList listaMilitiAbilitati = null
        ArrayList listaAllMiliti = null
        Milite milite

        if (croce) {
            listaAllMiliti = Milite.findAllByCroceAndAttivo(croce, true, [sort: 'cognome', order: 'asc'])
        }// fine del blocco if

        if (funzione && listaAllMiliti) {
            listaMilitiAbilitati = new ArrayList()
            listaAllMiliti?.each {
                milite = (Milite) it
                if (Militefunzione.findByCroceAndMiliteAndFunzione(croce, milite, funzione)) {
                    listaMilitiAbilitati.add(milite)
                }// fine del blocco if
            } // fine del ciclo each
        }// fine del blocco if

        return listaMilitiAbilitati
    }// fine del metodo

    //--lista dei militi abilitati alla funzione
    public ArrayList listaAutisti(Croce croce) {
        ArrayList listaMilitiAbilitati = null
        ArrayList listaAllMiliti = null
        Milite milite

        if (croce) {
            listaAllMiliti = Milite.findAllByCroceAndAttivo(croce, true, [sort: 'cognome', order: 'asc'])
        }// fine del blocco if

        if (funzione && listaAllMiliti) {
            listaMilitiAbilitati = new ArrayList()
            listaAllMiliti?.each {
                milite = (Milite) it
                if (Militefunzione.findByCroceAndMiliteAndFunzione(croce, milite, funzione)) {
                    listaMilitiAbilitati.add(milite)
                }// fine del blocco if
            } // fine del ciclo each
        }// fine del blocco if

        return listaMilitiAbilitati
    }// fine del metodo

    //--lista dei militi abilitati alla funzione
    public ArrayList listaSocDae(Croce croce) {
        ArrayList listaMilitiAbilitati = null
        ArrayList listaAllMiliti = null
        Milite milite

        if (croce) {
            listaAllMiliti = Milite.findAllByCroceAndAttivo(croce, true, [sort: 'cognome', order: 'asc'])
        }// fine del blocco if

        if (funzione && listaAllMiliti) {
            listaMilitiAbilitati = new ArrayList()
            listaAllMiliti?.each {
                milite = (Milite) it
                if (Militefunzione.findByCroceAndMiliteAndFunzione(croce, milite, funzione)) {
                    listaMilitiAbilitati.add(milite)
                }// fine del blocco if
            } // fine del ciclo each
        }// fine del blocco if

        return listaMilitiAbilitati
    }// fine del metodo

    //--lista dei militi abilitati alla funzione
    public ArrayList listaSoccorritori(Croce croce) {
        ArrayList listaMilitiAbilitati = null
        ArrayList listaAllMiliti = null
        Milite milite

        if (croce) {
            listaAllMiliti = Milite.findAllByCroceAndAttivo(croce, true, [sort: 'cognome', order: 'asc'])
        }// fine del blocco if

        if (funzione && listaAllMiliti) {
            listaMilitiAbilitati = new ArrayList()
            listaAllMiliti?.each {
                milite = (Milite) it
                if (Militefunzione.findByCroceAndMiliteAndFunzione(croce, milite, funzione)) {
                    listaMilitiAbilitati.add(milite)
                }// fine del blocco if
            } // fine del ciclo each
        }// fine del blocco if

        return listaMilitiAbilitati
    }// fine del metodo

    //--lista dei militi abilitati alla funzione
    public ArrayList listaBarellieri(Croce croce) {
        ArrayList listaMilitiAbilitati = null
        ArrayList listaAllMiliti = null
        Milite milite

        if (croce) {
            listaAllMiliti = Milite.findAllByCroceAndAttivo(croce, true, [sort: 'cognome', order: 'asc'])
        }// fine del blocco if

        if (funzione && listaAllMiliti) {
            listaMilitiAbilitati = new ArrayList()
            listaAllMiliti?.each {
                milite = (Milite) it
                if (Militefunzione.findByCroceAndMiliteAndFunzione(croce, milite, funzione)) {
                    listaMilitiAbilitati.add(milite)
                }// fine del blocco if
            } // fine del ciclo each
        }// fine del blocco if

        return listaMilitiAbilitati
    }// fine del metodo

    public int quantiGiorni() {
        int numGiorniIndietro = 7

        if (militeService.isLoggatoAdminOrMore()) {
            numGiorniIndietro = 1200
        }// fine del blocco if

        return numGiorniIndietro
    }// fine del metodo

    /**
     * Lista dei giorni a scalare da quello odierno
     */
    public ArrayList listaGiorniNum() {
        ArrayList listaGiorni = new ArrayList()
        int numGiorniIndietro = quantiGiorni()

        for (int k = 0; k < numGiorniIndietro; k++) {
            listaGiorni.add(k)
        } // fine del ciclo for

        return listaGiorni
    }// fine del metodo

    /**
     * Lista dei giorni a scalare da quello odierno
     */
    public ArrayList listaGiorniTxt() {
        ArrayList listaGiorni = new ArrayList()
        int numGiorniIndietro = quantiGiorni()
        Date giornoPartenza = Lib.creaDataOggi()
        Date giornoTmp = Lib.creaDataOggi()
        String dataTxt

        for (int k = 0; k < numGiorniIndietro; k++) {
            giornoTmp = giornoPartenza - k
            dataTxt = LibAmbTime.getSetGioMeseAnno(giornoTmp)
            listaGiorni.add(dataTxt)
        } // fine del ciclo for

        return listaGiorni
    }// fine del metodo

    /**
     * Controlla la validità dei campi inizio e fine
     * La fine deve essere SUCCESSIVA all'inizio
     */
    public static boolean isDurataCorretta(Viaggio viaggio) {
        boolean status = false
        Date dataIniziale
        Date dataFinale

        if (viaggio) {
            dataIniziale = viaggio.inizio
            dataFinale = viaggio.fine
            if (dataIniziale && dataIniziale) {
                status = LibAmbTime.isPrecedente(dataIniziale, dataFinale)
            }// fine del blocco if
        }// fine del blocco if

        return status
    }// fine del metodo

    /**
     * Durata di un viaggio
     */
    public static int durata(Viaggio viaggio) {
        int minuti = 0
        Date dataIniziale
        Date dataFinale

        if (viaggio) {
            dataIniziale = viaggio.inizio
            dataFinale = viaggio.fine
            if (dataIniziale && dataIniziale) {
                minuti = LibAmbTime.differenza(dataIniziale, dataFinale)
            }// fine del blocco if
        }// fine del blocco if

        return minuti
    }// fine del metodo

} // end of Service Class
