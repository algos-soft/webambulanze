package webambulanze

import grails.transaction.Transactional

@Transactional
class MilitefunzioneService {

    //--recupera i nomi di tutti gli utenti
    public ArrayList tuttiMilitiiDellaCroceSenzaProgrammatore(Croce croce) {
        ArrayList lista = new ArrayList()
        ArrayList listaTmp
        ArrayList listaMappa = new ArrayList()
        LinkedHashMap<String, ArrayList<Militefunzione>> mappa = new LinkedHashMap()
        Militefunzione militeFunzione
        String cognomeNome

        listaTmp = Militefunzione.findAllByCroce(croce)
        listaTmp?.each {
            militeFunzione = (Militefunzione) it
            cognomeNome = militeFunzione.milite.cognome + militeFunzione.milite.nome
            if (mappa.containsKey(cognomeNome)) {
                listaMappa = mappa.get(cognomeNome)
            } else {
                listaMappa = new ArrayList<Funzione>()
            }// end of if/else cycle
            listaMappa.add(militeFunzione)
            mappa.put(cognomeNome, listaMappa)
        } // fine del ciclo each

        if (mappa) {
            mappa.keySet().sort().each {
                listaMappa = mappa.get(it)
                listaMappa?.each {
                    lista.add(it)
                } // fine del ciclo each
            } // fine del ciclo each
        }// end of if cycle

        return lista
    }// fine del metodo

} // end of Service Class
