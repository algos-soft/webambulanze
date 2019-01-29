package webambulanze


class TipoTurnoService {

    public static ArrayList getListaTurni(Croce croce) {
        ArrayList listaTurni = null
        def turni
        TipoTurno turno

        turni = TipoTurno.findAllByCroce(croce)

        if (turni) {
            listaTurni = new ArrayList()
            turni?.each {
                turno = (TipoTurno) it
                listaTurni.add(turno.descrizione)
            } // fine del ciclo each
        }// fine del blocco if

        return listaTurni
    }// fine del metodo


    public static ArrayList getListaTurniId(Croce croce) {
        ArrayList listaTurniId = null
        def turni
        TipoTurno turno

        turni = TipoTurno.findAllByCroce(croce)

        if (turni) {
            listaTurniId = new ArrayList()
            turni?.each {
                turno = (TipoTurno) it
                listaTurniId.add(turno.id)
            } // fine del ciclo each
        }// fine del blocco if

        return listaTurniId
    }// fine del metodo


} // end of Service Class
