package webambulanze

import grails.plugin.springsecurity.userdetails.GrailsUser
import org.apache.commons.lang.time.FastDateFormat
import org.springframework.context.NoSuchMessageException
import org.springframework.util.StringUtils
import org.springframework.web.servlet.support.RequestContextUtils

import java.sql.Timestamp

class AmbulanzaTagLib {
    /**
     * Logica colori tabellone
     *
     * Titoli legenda
     * Titoli giorni
     *
     * Turno vuoto
     * Turno previsto
     * Turno assegnato
     * Turno libero
     * Turno critico
     * Turno bloccato
     * Turno effettuato
     * Turno orario
     *
     * Legenda tipo
     * Legenda funzione
     * Legenda orario
     */

    static namespace = "amb" //stand for ambulanze
    static String app = 'webambulanze'

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def militeService
    def croceService
    def springSecurityService

    public static String titoli = '440044'
    private static String turnoLiberoLontano = 'FFCC66'
    private static String turnoLiberoVicino = 'FF7733'
    private static String turnoLiberoCritico = 'FF3333'

    private static String turnoSegnatoValido = '63BA0B'
    private static String turnoSegnatoBloccato = '70AAFF'
    private static String turnoArchivio = 'c3c3c3'
    private static String turnoMancante = 'f8f8f8'
    public static String turni = 'C2E0D1'
    private static String orari = 'd3f1e2'

    private static int giorniVicini = 4
    private static int giorniCritici = 2
    private static int giorniBlocco = 2
    private static String aCapo = '<br>'
    private static String spazio = '&nbsp;'
    private static String spazioDoppio = spazio + spazio
    private static String spazioQuattro = spazioDoppio + spazioDoppio
    private static String spazioOtto = spazioQuattro + spazioQuattro
    private static String spazioLink = spazioOtto + spazioOtto + spazioQuattro
    private static String cellaVuota = '<td>' + spazio + '</td>'

    private static String SEP = '-'

    /**
     * Titolo della pagina <br>
     *
     * @return testo del tag
     */
    def titoloPaginaHome = {
        out << titoloPagina()
    }// fine della closure

    def ifModuloViaggi = { attrs, body ->
        String siglaCroce
        Croce croce

        if (params.siglaCroce) {
            siglaCroce = params.siglaCroce
            if (siglaCroce) {
                croce = Croce.findBySigla(siglaCroce)
                if (croce) {
                    if (croceService.usaModuloViaggi(croce)) {
                        out << body()
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if
    }// fine della closure

    /**
     * Titolo della pagina <br>
     *
     * @return testo del tag
     */
    def titoloPagina = {
        String testoOut = ''
        String testo = ''
        String siglaCroce
        Croce croce

        if (params.siglaCroce) {
            siglaCroce = params.siglaCroce
            if (siglaCroce) {
                croce = Croce.findBySigla(siglaCroce)
            }// fine del blocco if
        }// fine del blocco if

        if (croce) {
            testo += cellaTitoloImmagine(croce)
            testo += '\n'
            testo += cellaTitoloCroce(croce)
            testo += '\n'
            testo += cellaTitoloLogin()
            testoOut += Lib.tagTable(testo)
            testoOut = testoOut.trim()
        }// fine del blocco if

        out << testoOut
    }// fine della closure

    private static String cellaTitoloImmagine(Croce croce) {
        String testo = ''
        String nomeLink = ''
        String fileImmagine = ''
        String testoImmagine = ''
        String testoLink = ''

        if (croce) {
            nomeLink = croce.organizzazione.wiki
            if (croce.organizzazione.fileLogo) {
                fileImmagine = croce.organizzazione.fileLogo
                if (fileImmagine) {
                    testoLink += '<a href="'
                    testoLink += nomeLink
                    testoLink += '">'

                    testoImmagine += '<img src="/webambulanze/images/'
                    testoImmagine += fileImmagine
                    testoImmagine += '"/>'

                    testo += testoLink
                    testo += testoImmagine
                    testo += '</a>'
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return Lib.tagCellaTitolo(testo, Aspetto.titoloimmagine)
    }// fine del metodo

    private static String cellaTitoloCroce(Croce croce) {
        String testo = ''

        if (croce) {
            testo = croce.descrizione
        }// fine del blocco if

        return Lib.tagCellaTitolo(testo, Aspetto.titolocroce)
    }// fine del metodo

    private String cellaTitoloLogin() {
        String testo = ''
        String tagAnonimo = 'anonymousUser'
        String nomeAnonimo = "Sei collegato come 'anonimo'"
        Utente utente
        long utenteId
        Milite milite
        def user = springSecurityService.principal
        def pippo = springSecurityService.getCurrentUser()

        if (user && user instanceof String && user.equals(tagAnonimo)) {
            testo = nomeAnonimo
        } else {
            if (user instanceof GrailsUser) {
                if (user.id) {
                    utenteId = (long) user.id
                    utente = Utente.findById(utenteId)
                    if (utente) {
                        if (utente.milite) {
                            milite = utente.milite
                        }// fine del blocco if
                    }// fine del blocco if
                }// fine del blocco if

                if (milite) {
                    testo = 'Ciao, ' + milite.nome + ' ' + milite.cognome
                } else {
                    if (utente) {
                        testo = 'Ciao, ' + utente.nickname
                    } else {
                        testo = 'Ciao, ' + user.username
                    }// fine del blocco if-else
                }// fine del blocco if-else
            }// fine del blocco if
        }// fine del blocco if-else

        return Lib.tagCellaTitolo(testo, Aspetto.titolologin)
    }// fine del metodo

    /**
     * Tabella dei turni <br>
     *
     * La riga dei titoli ha 8 colonne (7 giorni + mese/anno
     * Il corpo della tavola ha 9 colonne (7 giorni + 2 colonne di legenda)
     *
     * @param mappa parametri
     *        dataInizio: giorno iniziale del periodo da considerare
     *        dataFine:   giorno finale del periodo da considerare
     * @return testo del tag
     */
    def tabella = { mappa ->
        String testoOut
        String testo = ''
        Croce croce = null
        Date inizio = null
        Date fine = null
        boolean sempreCreabile = false
        boolean militePuoCreareTurnoStandard = false
        boolean militePuoCreareTurnoExtra = false
        boolean usaNomeCognome = false

        if (params.siglaCroce) {
            usaNomeCognome = croceService.usaNomeCognome(params.siglaCroce)
            croce = Croce.findBySigla((String) params.siglaCroce)
        }// fine del blocco if
        if (mappa.dataInizio && mappa.dataInizio instanceof Date) {
            inizio = (Date) mappa.dataInizio
        }// fine del blocco if
        if (mappa.dataFine && mappa.dataFine instanceof Date) {
            fine = (Date) mappa.dataFine
        }// fine del blocco if

        //--serve per colorare di grigio i turni non creabili quando sono collegato come Milite
        if (militeService?.isLoggatoAdminOrMore()) {
            sempreCreabile = true
        }// fine del blocco if
        militePuoCreareTurnoStandard = croceService.militePuoCreareTurnoStandard(croce)
        militePuoCreareTurnoExtra = croceService.militePuoCreareTurnoExtra(croce)

//        testo += this.captionTabella(params)
        testo += titoliTabella(inizio, fine)
        testo += corpoTabella(croce, inizio, fine, sempreCreabile, militePuoCreareTurnoStandard, militePuoCreareTurnoExtra, usaNomeCognome)
        testo += rigaBordo()
        testo += legenda()
        testo += copyright()

        testoOut = Lib.tagTable(testo)

        out << testoOut
    }// fine della closure

    /**
     * Corpo della tabella <br>
     *
     * @param croce di riferimento
     * @param inizio : giorno iniziale del periodo da considerare
     * @param fine :   giorno finale del periodo da considerare
     * @return testo del tag
     */
    private static String corpoTabella(
            Croce croce,
            Date inizio,
            Date fine,
            boolean sempreCreabile,
            boolean militePuoCreareTurnoStandard,
            boolean militePuoCreareTurnoExtra,
            boolean usaNomeCognome) {
        String testoBody = ''
        def tipiTurno = null
        int pariDispari = 0
        int numTipoTurno = 0
        int numExtra

        if (croce && inizio && fine) {
            tipiTurno = TipoTurno.findAllByCroceAndVisibile(croce, true, [sort: 'ordine', order: 'asc'])
        }// fine del blocco if

        //--un pacchetto per ogni tipo di turno di questa croce
        if (tipiTurno) {
            tipiTurno?.each {
                numTipoTurno++
                pariDispari++
                numExtra = 1

                if (it.primo) {
                    testoBody += rigaBordo()
                }// fine del blocco if
                testoBody += righeDiUnTurno(it, inizio, fine, pariDispari, numExtra, sempreCreabile, militePuoCreareTurnoStandard, militePuoCreareTurnoExtra, usaNomeCognome)

                if (it.multiplo) {
                    while (esisteUnTurnoNellaUltimaRiga(it, inizio, fine, numExtra)) {
                        pariDispari++
                        numExtra++
                        testoBody += righeDiUnTurno(it, inizio, fine, pariDispari, numExtra, sempreCreabile, militePuoCreareTurnoStandard, militePuoCreareTurnoExtra, usaNomeCognome)
                    }// fine del blocco while
                }// fine del blocco if
            }// end of each
        }// end of if

        return Lib.getBody(testoBody)
    }// fine del metodo

    /**
     * Righe (variabili secondo il tipo di turno) della tabella per un turno <br>
     *
     * @param tipoTurno
     * @param dataInizio : giorno iniziale del periodo da considerare
     * @param dataFine :   giorno finale del periodo da considerare
     * @return testo del tag
     */
    private static String righeDiUnTurno(
            TipoTurno tipoTurno,
            Date inizio,
            Date fine,
            int pariDispari,
            int numExtra,
            boolean sempreCreabile,
            boolean militePuoCreareTurnoStandard,
            boolean militePuoCreareTurnoExtra,
            boolean usaNomeCognome) {
        String testoRiga = ''
        LinkedHashMap<Date, Turno> mappaTurniDiUnTipo = creaMappaTurniDiUnTipo(tipoTurno, inizio, fine, numExtra)
        ArrayList listaTurni = listaTurni(tipoTurno, inizio, fine)
        String funzioniTxt
        ArrayList funzioni
        int giorni

        //ciclo di righe corrispondenti alle funzioni previste in questo tipo di turno
        funzioni = tipoTurno.getListaFunzioni()
        if (funzioni) {
            // riga descrizione/orario
            //--per questo tipo di turno, crea una riga iniziale
            //--la prima cella è l'orario del turno
            //--seguono sette celle vuote (oppure con ripetuto l'orario)
            giorni = (fine - inizio) + 1
            testoRiga = rigaOrario(giorni, tipoTurno, funzioni.size() + 1, pariDispari)

            //--per ogni funzione prevista, crea una riga
            //--la prima cella è il nome della funzione
            //--seguono sette celle di turno
            int pos = funzioni.size()
            funzioni?.each {
                pos--
//                if (bordo && pos == 0) {
//                    testoRiga += this.rigaTurno(mappaTurniDiUnTipo, (Funzione) it, tipoTurno, true)
//                } else {
//                    testoRiga += this.rigaTurno(mappaTurniDiUnTipo, (Funzione) it, tipoTurno, false)
//                }// fine del blocco if-else
                testoRiga += rigaTurno(mappaTurniDiUnTipo, (Funzione) it, tipoTurno, sempreCreabile, militePuoCreareTurnoStandard, militePuoCreareTurnoExtra, usaNomeCognome)
            } // fine del ciclo each

//            for (int k = 0; k < max; k++) {
//                if (bordo && (k = max - 1)) {
//                    testoRiga += this.rigaTurno(mappaTurniDiUnTipo, funzioni[k], tipoTurno, true)
//                } else {
//                    testoRiga += this.rigaTurno(mappaTurniDiUnTipo, funzioni[k], tipoTurno, false)
//                }// fine del blocco if-else
//            } // fine del ciclo for

//            funzioni?.each {
//                testoRiga += this.rigaTurno(mappaTurniDiUnTipo, (Funzione) it, tipoTurno, bordo)
//            } // fine del ciclo each
        }// fine del blocco if

        return testoRiga
    }// fine del metodo

    //--disegna una riga di separazione per tutte le celle (1+7)
    private static String rigaBordo() {
        String testo = ''
        int giorni = 7
        giorni++

        for (int k = 0; k <= giorni; k++) {
            testo += Lib.tagCella('', Aspetto.bordo, 1)
        } // fine del ciclo for

        return testo
    }// fine del metodo

    //--disegna i menu extra aggiuntivi a quelli standard dei templates
    //--stringa solo azione e di default controller=questo; classe e titolo vengono uguali
    //--mappa con [cont:'controller', action:'metodo', icon:'iconaImmagine', title:'titoloVisibile']
    def menuExtra = { args ->
        String testoOut = ''
        def lista = null
        def elementoLista
        String app = 'webambulanze'
        String contQuesto = ''
        String cont = ''
        String action = ''
        String icon = ''
        String title = ''

        if (args.menuExtra) {
            lista = args.menuExtra
        }// fine del blocco if
        if (params.controller) {
            contQuesto = params.controller
        }// fine del blocco if

        lista?.each {
            elementoLista = it
            action = ''
            if (elementoLista instanceof String) {
                if (elementoLista) {
                    cont = contQuesto
                    action = elementoLista
                    icon = action
                    title = action
//                    title = LibTesto.primaMaiuscola(title)
                }// fine del blocco if
            } else {
                if (elementoLista instanceof Map && elementoLista.action) {
                    cont = contQuesto
                    if (elementoLista.cont) {
                        cont = elementoLista.cont
                    }// fine del blocco if
                    action = ''
                    if (elementoLista.action) {
                        action = elementoLista.action
                    }// fine del blocco if
                    icon = action
                    if (elementoLista.icon) {
                        icon = elementoLista.icon
                    }// fine del blocco if
                    title = action
//                    title = LibTesto.primaMaiuscola(title)
                    if (elementoLista.title) {
                        title = elementoLista.title
                    }// fine del blocco if
                } else { // emergency error
                    cont = contQuesto
                    action = 'list'
                    icon = action
                    title = 'error'
                }// fine del blocco if-else
            }// fine del blocco if-else
            if (action) {
                testoOut += getRigaMenu(app, cont, action, icon, title)
            }// fine del blocco if
        } // fine del ciclo each

        out << testoOut
    }// fine della closure

    String getRigaMenu(app, cont, action, icon, title) {
        String testoOut = ''

        testoOut += "<li>"
        testoOut += "<a "
        testoOut += "href=\"/${app}/${cont}/${action}\" "
        testoOut += "class=\"${action}\""
        testoOut += ">"
        testoOut += title
        testoOut += "</a>"
        testoOut += "</li>"

        return testoOut
    }// fine della closure

    //--disegna i menu aggiuntivi (oltre a quelli standard del GPS tipico)
    def menuExtraOld = { args ->
        String testoOut = ''
        def menuExtra
        String className
        String controller
        String action
        String message
        def id

        if (args.menuExtra) {
            menuExtra = args.menuExtra
        }// fine del blocco if

        if (args.id) {
            id = args.id
        }// fine del blocco if

        if (menuExtra && menuExtra instanceof Map) {
            def temp = menuExtra
            menuExtra = new ArrayList()
            menuExtra.add(temp)
        }// fine del blocco if

        if (menuExtra) {
            menuExtra?.each {
                testoOut = ''
                className = it.class
                controller = it.controller
                action = it.action
                message = it.message

                if (className && controller && action) {
                    testoOut += "<a "
                    testoOut += "href=\"/webambulanze/${controller}/${action}/${id}\" "
                    testoOut += "class=\"${className}\""
                    testoOut += ">"
                    testoOut += message
                    testoOut += "</a>"
                }// fine del blocco if

            } // fine del ciclo each
        }// fine del blocco if

        out << testoOut
    }// fine della closure

    //--disegna i titoli delle colonne della tavola/lista
    def titoliLista = { args ->
        String testoOut = ''
        ArrayList lista = null
        def bean = null
        String contPath = 'webambulanze.'
        def campi
        String cont
        String oldSort = 'id'
        String sort = ''
        String order = 'asc'
        String title = 'void'
        String campo
        def elementoLista

        if (args.campiLista) {
            lista = args.campiLista
        }// fine del blocco if
        if (params.controller) {
            cont = params.controller
            contPath += Lib.primaMaiuscola(cont)
        }// fine del blocco if
        if (params.sort) {
            oldSort = params.sort
        }// fine del blocco if
        if (params.order) {
            order = params.order
        }// fine del blocco if

        if (cont) {
            if (lista) {
                lista?.each {
                    elementoLista = it
                    if (elementoLista instanceof String) {
                        campo = it
                        sort = campo
//                        title = message(code: "${cont}.${campo}.labellist")@todo non funge
                        title = campo.toString()
                    } else {
                        if (elementoLista instanceof Map && elementoLista.titolo && elementoLista.campo) {
                            campo = elementoLista.campo
                            sort = elementoLista.campo
                            title = elementoLista.titolo
                        } else { // emergency error
                            campo = 'it'
                            sort = 'asc'
                            title = 'ID'
                        }// fine del blocco if-else
                    }// fine del blocco if-else

                    if (sort.equals(oldSort)) {
                        testoOut += Lib.getTitoloTabellaNotSorted(app, cont, sort, order, title)
                    } else {
                        testoOut += Lib.getTitoloTabellaSorted(app, cont, sort, order, title)
                    }// fine del blocco if-else
                } // fine del ciclo each
            } else {
                bean = applicationContext.getBean(contPath)
                if (bean) {
                    campi = bean.properties.keySet()
                    campi?.each {
                        campo = it
                        sort = campo
                        title = message(code: "${cont}.${campo}.labellist")
                        if (sort.equals(oldSort)) {
                            testoOut += Lib.getTitoloTabellaNotSorted(app, cont, sort, order, title)
                        } else {
                            testoOut += Lib.getTitoloTabellaSorted(app, cont, sort, order, title)
                        }// fine del blocco if-else
                    } // fine del ciclo each
                }// fine del blocco if
            }// fine del blocco if-else
        }// fine del blocco if

        out << testoOut
    }// fine della closure

    //--disegna i titoli delle colonne della tavola/lista
    def titoliExtraLista = { args ->
        String testoOut = ''
        ArrayList lista = null
        String cont
        String oldSort = 'id'
        String sort
        String order = 'asc'
        String title = 'void'
        String campo

        if (args.campiExtra) {
            lista = args.campiExtra
        }// fine del blocco if
        if (params.controller) {
            cont = params.controller
        }// fine del blocco if
        if (params.sort) {
            oldSort = params.sort
        }// fine del blocco if
        if (params.order) {
            order = params.order
        }// fine del blocco if

        if (cont) {
            lista?.each {
                campo = it
                sort = campo
                //#todo PATCH
                sort = 'id'
                //#todo PATCH
                title = message(code: "${cont}.${campo}.labellist")
                if (sort.equals(oldSort)) {
                    testoOut += Lib.getTitoloTabellaNotSorted(app, cont, '', '', title)
                } else {
                    testoOut += Lib.getTitoloTabellaSorted(app, cont, '', '', title)
                }// fine del blocco if-else
            } // fine del ciclo each
        }// fine del blocco if

        out << testoOut
    }// fine della closure

    //--disegna tutti i campi di una riga della tavola/lista
    //--disegna eventuali campi extra per le funzioni dei militi di una croce
    def rigaLista = { args ->
        String testoOut = ''
        ArrayList lista = null
        ArrayList listaExtra = null
        def bean = null
        String contPath = 'webambulanze.'
        def campi
        String cont
        String campo = ''
        def rec = null
        long id = 0.0
        def value = null
        ArrayList nomeFunzioniAttiveDelMilite = null
        def elementoLista
        String action
        Croce croce
        def a = args

        if (params.siglaCroce) {
            croce = Croce.findBySigla((String) params.siglaCroce)
        }// fine del blocco if
        if (args.campiLista) {
            lista = args.campiLista
        }// fine del blocco if
        if (params.controller) {
            cont = params.controller
            contPath += Lib.primaMaiuscola(cont)
        }// fine del blocco if
        if (args.rec) {
            rec = args.rec
        }// fine del blocco if
        try { // prova ad eseguire il codice
            if (rec && rec.id) {
                id = rec.id
            }// fine del blocco if
        } catch (Exception unErrore) { // intercetta l'errore
            log.error unErrore
        }// fine del blocco try-catch
        if (args.campiExtra) {
            listaExtra = args.campiExtra
        }// fine del blocco if

        //--@todo PATCH per utenteRuolo
        if (!id && croce && croce.sigla.equals(Cost.CROCE_PUBBLICA_PIANORO && cont.equals('UtenteRuolo'))) {
            String utente = rec.utente
            String ruolo = rec.ruolo
            def stop
        }// fine del blocco if


        if (croce && rec && rec instanceof Milite) {
            nomeFunzioniAttiveDelMilite = militeService.nomeFunzioniPerMilite(croce, rec)
        }// fine del blocco if

        if (cont && rec) {
            if (cont.equals('militestatistiche')) {
                cont = 'militeturno'
            }// fine del blocco if

            if (lista) {
                lista?.each {
                    elementoLista = it
                    if (elementoLista instanceof String) {
                        campo = it
                    } else {
                        if (elementoLista instanceof Map && elementoLista.campo) {
                            campo = elementoLista.campo
                        }// fine del blocco if
                    }// fine del blocco if-else
                    if (rec."${campo}") {
                        value = rec."${campo}"
                    } else {
                        value = null
                    }// fine del blocco if-else

                    if (cont.equals('militeturno')) {
                        action = 'dettagli'
                        id = rec.milite.id
                        testoOut += Lib.getCampoTabella(app, cont, id, value, action)
                    } else {
                        testoOut += Lib.getCampoTabella(app, cont, id, value)
                    }// fine del blocco if-else
                } // fine del ciclo each
            } else {
                bean = applicationContext.getBean(contPath)
                if (bean) {
                    campi = bean.properties.keySet()
                    campi?.each {
                        campo = it
                        if (rec."${campo}") {
                            value = rec."${campo}"
                        } else {
                            value = null
                        }// fine del blocco if-else
                        testoOut += Lib.getCampoTabella(app, cont, id, value)
                    } // fine del ciclo each
                }// fine del blocco if
            }// fine del blocco if-else
        }// fine del blocco if

        if (cont && rec && listaExtra) {
            value = false
            listaExtra?.each {
                campo = it
                if (nomeFunzioniAttiveDelMilite.contains(campo)) {
                    testoOut += Lib.getCampoTabella(app, cont, id, true)
                } else {
                    testoOut += Lib.getCampoTabella(app, cont, id, false)
                }// fine del blocco if-else

            } // fine del ciclo each
        }// fine del blocco if

        out << testoOut
    }// fine della closure

    //--disegna tutti i campi extra per le funzioni dei militi di una croce
    def extraScheda = { args ->
        String testoOut = ''
        ArrayList listaExtra = null
        ArrayList nomeFunzioniAttiveDelMilite = null
        String cont
        String campo
        Milite milite = null
        String desc = ''
        Funzione funz = null
        Croce croce

        if (params.siglaCroce) {
            croce = Croce.findBySigla((String) params.siglaCroce)
        }// fine del blocco if
        if (args.rec) {
            milite = (Milite) args.rec
        }// fine del blocco if
        if (args.campiExtra) {
            listaExtra = args.campiExtra
        }// fine del blocco if

        if (milite && croce) {
            nomeFunzioniAttiveDelMilite = militeService.nomeFunzioniPerMilite(croce, milite)
        }// fine del blocco if

        if (croce && listaExtra) {
            listaExtra?.each {
                desc = ''
                campo = it
                funz = Funzione.findByCroceAndSigla(croce, campo)
                if (funz) {
                    desc = funz.descrizione
                }// fine del blocco if

                if (nomeFunzioniAttiveDelMilite.contains(campo)) {
                    testoOut += Lib.getCampoSchedaBooleano(campo, desc, true)
                } else {
                    testoOut += Lib.getCampoSchedaBooleano(campo, desc, false)
                }// fine del blocco if-else
            } // fine del ciclo each
        }// fine del blocco if

        out << testoOut
    }// fine della closure

//--disegna tutti i campi extra per le funzioni dei militi di una croce in modalita form (edit e create)
    def extraSchedaForm = { args ->
        String testoOut = ''
        ArrayList listaExtra = null
        ArrayList nomeFunzioniAttiveDelMilite = null
        String cont
        String campo
        Milite milite = null
        def value = null
        String desc = ''
        Funzione funz = null
        Croce croce

        if (params.siglaCroce) {
            croce = Croce.findBySigla((String) params.siglaCroce)
        }// fine del blocco if
        if (args.rec) {
            milite = (Milite) args.rec
        }// fine del blocco if
        if (args.campiExtra) {
            listaExtra = args.campiExtra
        }// fine del blocco if

        if (croce && milite) {
            if (milite.id) {
                nomeFunzioniAttiveDelMilite = militeService.nomeFunzioniPerMilite(croce, milite)
            } else {
                nomeFunzioniAttiveDelMilite = []
            }// fine del blocco if-else
        }// fine del blocco if

        if (croce && listaExtra) {
            listaExtra?.each {
                desc = ''
                campo = it
                funz = Funzione.findByCroceAndSigla(croce, campo)
                if (funz) {
                    desc = funz.descrizione
                }// fine del blocco if

                if (nomeFunzioniAttiveDelMilite && nomeFunzioniAttiveDelMilite.contains(campo)) {
                    testoOut += Lib.getCampoSchedaFormBooleano(campo, desc, true)
                } else {
                    testoOut += Lib.getCampoSchedaFormBooleano(campo, desc, false)
                }// fine del blocco if-else
            } // fine del ciclo each
        }// fine del blocco if

        out << testoOut
    }// fine della closure

    //--disegna tutti i campi di una riga della tavola/lista
    //--disegna eventuali campi extra per le funzioni dei militi di una croce
    def rigaListaStatistiche = { args ->
        String testoOut = ''
        ArrayList lista = null
        ArrayList listaExtra = null
        def bean = null
        String contPath = 'webambulanze.'
        def campi
        String cont
        String campo
        def rec = null
        long id = 0.0
        def value = null
        ArrayList nomeFunzioniAttiveDelMilite = null
        int turniAnno = 0
        int oreAnno = 0
        String tagTurni = ''
        String tagOre = ''

        if (args.campiLista) {
            lista = args.campiLista
        }// fine del blocco if
        if (params.controller) {
            cont = params.controller
            contPath += Lib.primaMaiuscola(cont)
        }// fine del blocco if
        if (args.rec) {
            rec = args.rec
        }// fine del blocco if
        try { // prova ad eseguire il codice
            if (rec && rec.id) {
                id = rec.id
            }// fine del blocco if
        } catch (Exception unErrore) { // intercetta l'errore
            log.error unErrore
        }// fine del blocco try-catch
        if (args.campiExtra) {
            listaExtra = args.campiExtra
        }// fine del blocco if

        if (rec && rec instanceof Milite) {
            nomeFunzioniAttiveDelMilite = militeService.nomeFunzioniPerMilite(rec)
        }// fine del blocco if

        if (cont && rec) {
            if (lista) {
                lista?.each {
                    campo = it
                    if (rec."${campo}") {
                        value = rec."${campo}"
                    } else {
                        value = null
                    }// fine del blocco if-else

                    testoOut += Lib.getCampoTabella(app, cont, id, value)
                } // fine del ciclo each
            } else {
                bean = applicationContext.getBean(contPath)
                if (bean) {
                    campi = bean.properties.keySet()
                    campi?.each {
                        campo = it
                        if (rec."${campo}") {
                            value = rec."${campo}"
                        } else {
                            value = null
                        }// fine del blocco if-else
                        testoOut += Lib.getCampoTabella(app, cont, id, value)
                    } // fine del ciclo each
                }// fine del blocco if
            }// fine del blocco if-else
        }// fine del blocco if

        if (cont && rec && listaExtra) {
            //turniAnno = militeService.turniAnno((Milite) rec)
            //oreAnno = militeService.oreAnno((Milite) rec)
            value = false
            listaExtra?.each {
                campo = it
                if (nomeFunzioniAttiveDelMilite.contains(campo)) {
                    testoOut += Lib.getCampoTabella(app, cont, id, '?')
                } else {
//                    if (campo.equals(Cost.CAMPO_TURNI) || campo.equals(Cost.CAMPO_ORE)) {
//                        if (campo.equals(Cost.CAMPO_TURNI)) {
//                            testoOut += Lib.getCampoTabella(app, cont, id, turniAnno)
//                        }// fine del blocco if
//                        if (campo.equals(Cost.CAMPO_ORE)) {
//                            testoOut += Lib.getCampoTabella(app, cont, id, oreAnno)
//                        }// fine del blocco if
//                    } else {
//                        testoOut += Lib.getCampoTabella(app, cont, id, '-')
//                    }// fine del blocco if-else
                }// fine del blocco if-else
            } // fine del ciclo each
        }// fine del blocco if

        out << testoOut
    }// fine della closure

    /**
     * Caption sopra la tabella <br>
     */
    private String captionTabella(def params) {
        String testoOut = ''
        String testoCaption = ''

        testoOut += Lib.getCaption(testoCaption)

        return testoOut
    }// fine del metodo

    /**
     * Outputs the given <code>Date</code> object in the specified format. If
     * the <code>date</code> is not given, then the current date/time is used.
     * If the <code>format</code> option is not given, then the date is output
     * using the default format.<br/>
     *
     * e.g., &lt;g:formatDate date="${myDate}" format="yyyy-MM-dd HH:mm" /&gt;<br/>
     *
     * @see java.text.SimpleDateFormat
     *
     * @attr date the date object to display; defaults to now if not specified
     * @attr format The formatting pattern to use for the date, see SimpleDateFormat
     * @attr formatName Look up format from the default MessageSource / ResourceBundle (i18n/*.properties file) with this key. If format and formatName are empty, format is looked up with 'default.date.format' key. If the key is missing, 'yyyy-MM-dd HH:mm:ss z' formatting pattern is used.
     * @attr type The type of format to use for the date / time. format or formatName aren't used when type is specified. Possible values: 'date' - shows only date part, 'time' - shows only time part, 'both'/'datetime' - shows date and time
     * @attr timeZone the time zone for formatting. See TimeZone class.
     * @attr locale Force the locale for formatting.
     * @attr style Use default date/time formatting of the country specified by the locale. Possible values: SHORT (default), MEDIUM, LONG, FULL . See DateFormat for explanation.
     * @attr dateStyle Set separate style for the date part.
     * @attr timeStyle Set separate style for the time part.
     */
    def formatDate = { attrs ->
        def date
        if (attrs.containsKey('date')) {
            date = attrs.date
            if (date == null) return
        } else {
            date = new Date()
        }

        def locale = resolveLocale(attrs.locale)
        String timeStyle = null
        String dateStyle = null
        if (attrs.style != null) {
            String style = attrs.style.toString().toUpperCase()
            timeStyle = style
            dateStyle = style
        }

        if (attrs.dateStyle != null) {
            dateStyle = attrs.dateStyle.toString().toUpperCase()
        }
        if (attrs.timeStyle != null) {
            timeStyle = attrs.timeStyle.toString().toUpperCase()
        }
        String type = attrs.type?.toString()?.toUpperCase()
        def formatName = attrs.formatName
        def format = attrs.format
        def timeZone = attrs.timeZone
        if (timeZone != null) {
            if (!(timeZone instanceof TimeZone)) {
                timeZone = TimeZone.getTimeZone(timeZone as String)
            }
        } else {
            timeZone = TimeZone.getDefault()
        }

        def dateFormat
        if (!type) {
            if (!format && formatName) {
                format = messageHelper(formatName, null, null, locale)
                if (!format) {
                    throwTagError("Attribute [formatName] of Tag [formatDate] specifies a format key [$formatName] that does not exist within a message bundle!")
                }
            } else if (!format) {
                format = messageHelper('date.format', {
                    messageHelper('amb.date.format', 'MM-dd',
                            null, locale)
                }, null, locale)
            }
            dateFormat = FastDateFormat.getInstance(format, timeZone, locale)
        } else {
            if (type == 'DATE') {
                dateFormat = FastDateFormat.getDateInstance(parseStyle(dateStyle), timeZone, locale)
            } else if (type == 'TIME') {
                dateFormat = FastDateFormat.getTimeInstance(parseStyle(timeStyle), timeZone, locale)
            } else { // 'both' or 'datetime'
                dateFormat = FastDateFormat.getDateTimeInstance(parseStyle(dateStyle), parseStyle(timeStyle), timeZone, locale)
            }
        }

        //if (type == 'DATE') {
        //    dateFormat = FastDateFormat.getInstance('MM-dd')
        //}
        //else if (type == 'TIME') {
        //    dateFormat = FastDateFormat.getInstance('MM-dd')
        //}
        //else { // 'both' or 'datetime'
        //    dateFormat = FastDateFormat.getInstance('MM-dd')
        //}

        String formattata = Lib.presentaDataMese(attrs.date)
        out << formattata
///        return dateFormat.format(date)
    }// fine della closure


    def formatTempo = { attrs ->
        String testoOut = ''
        def giorno
        def ore
        def minuti

        if (attrs.containsKey('date')) {
            giorno = attrs.date
            if (giorno == null) return
        } else {
            giorno = new Date()
        }

        if (giorno) {
            ore = Lib.getNumOra(giorno)
            minuti = Lib.getNumMinuti(giorno)
            testoOut = ore + ':' + minuti
        }// fine del blocco if

        out << testoOut
    }// fine della closure

    def resolveLocale(localeAttr) {
        def locale = localeAttr
        if (locale != null && !(locale instanceof Locale)) {
            locale = StringUtils.parseLocaleString(locale as String)
        }
        if (locale == null) {
            locale = RequestContextUtils.getLocale(request)
            if (locale == null) {
                locale = Locale.getDefault()
            }
        }
        return locale
    }// fine del metodo


    String messageHelper(code, defaultMessage = null, args = null, locale = null) {
        if (locale == null) {
            locale = RequestContextUtils.getLocale(request)
        }
        def messageSource = grailsAttributes.applicationContext.messageSource
        def message
        try {
            message = messageSource.getMessage(code, args == null ? null : args.toArray(), locale)
        }
        catch (NoSuchMessageException e) {
            if (defaultMessage != null) {
                if (defaultMessage instanceof Closure) {
                    message = defaultMessage()
                } else {
                    message = defaultMessage as String
                }
            }
        }
        return message
    }// fine del metodo

    /**
     * Lista dei controllers per la videata Home <br>
     * Presento i controller in ordine da quelli disponibili al programmatore in giù
     *
     * @return testo del tag
     */
    def listaControllers = {
        String testoOut = ''
        String listaControlliTxt
        def lista
        boolean usaModuloViaggi = croceService.usaModuloViaggi((String) params.siglaCroce)

        if (params.siglaCroce) {
            listaControlliTxt = croceService.getControlli((String) params.siglaCroce)
        }// fine del blocco if

        if (listaControlliTxt) {
            lista = listaControlliTxt.split(',')
            lista?.each {
                testoOut += Lib.tagController(it)
            } // fine del ciclo each
        } else {
            if (militeService.isLoggatoProgrammatore()) {
                testoOut += '<h2>Moduli disponibili al programmatore:</h2>'
                testoOut += Lib.tagController('Versione', 'Lista versioni installate')
                testoOut += Lib.tagController('Militestatistiche', 'Forza calcolo statistiche', 'calcola')
                testoOut += Lib.tagController('Turno', 'Lista turni (non tabellone)', 'list')
            }// fine del blocco if
            if (militeService.isLoggatoCustodeOrMore()) {
                testoOut += '<h2>Moduli disponibili al custode:</h2>'
                testoOut += Lib.tagController('Utente', 'Utenti e passwords')
            }// fine del blocco if
            if (militeService.isLoggatoAdminOrMore()) {
                testoOut += '<h2>Moduli disponibili agli admin:</h2>'
                testoOut += Lib.tagController('Ruolo', 'Ruoli')
                testoOut += Lib.tagController('UtenteRuolo', 'Tavola incrocio utenti-ruolo', 'list')
                testoOut += Lib.tagController('Militefunzione', 'Tavola incrocio militi-funzioni')
                testoOut += Lib.tagController('Croce', 'Croci')
                testoOut += Lib.tagController('Settings', 'Preferenze')
                testoOut += Lib.tagController('Logo', 'Logs')
                testoOut += Lib.tagController('Militeturno', 'Statistiche dettagliate')
            }// fine del blocco if
            if (militeService.isLoggatoMiliteOrMore()) {
                testoOut += '<h2>Moduli disponibili ai militi:</h2>'
                if (usaModuloViaggi) {
                    testoOut += Lib.tagController('Automezzo', 'Automezzi')
                }// fine del blocco if
                testoOut += Lib.tagController('Funzione', 'Funzioni')
                testoOut += Lib.tagController('TipoTurno', 'Tipologia turni')
                testoOut += Lib.tagController('Milite', 'Militi')
                testoOut += Lib.tagController('Militestatistiche', 'Statistiche')
                if (usaModuloViaggi) {
                    testoOut += Lib.tagController('Viaggio', 'Viaggi effettuati')
                }// fine del blocco if
            }// fine del blocco if
            testoOut += '<h2>Moduli sempre visibili:</h2>'
            testoOut += Lib.tagController('Gen', 'Tabellone turni')

        }// fine del blocco if-else

        out << testoOut
    }// fine della closure

    /**
     * Titoli di testa della tabella <br>
     *
     * @param inizio : giorno iniziale del periodo da considerare
     * @param fine :   giorno finale del periodo da considerare
     * @return testo del tag
     */
    private static String titoliTabella(Date inizio, Date fine) {
        String testoOut = ''
        String testoLegenda
        String testoGiorni

        if (inizio && fine) {
            testoLegenda = titoliTabellaLegenda(inizio, fine)
            testoGiorni = titoliTabellaGiorni(inizio, fine)
            testoOut += testoLegenda
            testoOut += testoGiorni
        }// end of if

        testoOut = Lib.tagRiga(testoOut)
        testoOut = Lib.tagHead(testoOut)

        return testoOut
    }// fine del metodo

    /**
     * Titoli di testa della tabella <br>
     * Prima colonna legenda
     *
     * @param inizio : giorno iniziale del periodo da considerare
     * @param fine :   giorno finale del periodo da considerare
     * @return testo del tag
     */
    private static String titoliTabellaLegenda(Date inizio, Date fine) {
        String testoOut = ''
        String testoTurni
        String testoMese
        int colonneLegendaSinistra = 2 //Tipo turno e funzione
        int giorni
        def giorno

        if (inizio && fine) {
            testoTurni = 'Turni di servizio '
            testoMese = getMeseCorrente(inizio);
            testoOut = Lib.tagCellaTitolo(testoTurni + testoMese, Aspetto.titolilegenda, colonneLegendaSinistra)
        }// end of if

        return testoOut
    }// fine del metodo

    /**
     * Titoli di testa della tabella <br>
     * Colonne (7) dei giorni
     *
     * @param inizio : giorno iniziale del periodo da considerare
     * @param fine :   giorno finale del periodo da considerare
     * @return testo del tag
     */
    private static String titoliTabellaGiorni(Date inizio, Date fine) {
        String testoOut = ''
        String testoGiorni = ''
        String testoRigaTitolo = ''
        int giorni
        def giorno

        if (inizio && fine) {
            giorni = fine - inizio
            for (int k = 0; k <= giorni; k++) {
                giorno = inizio + k
                testoRigaTitolo += titoloGiorno(giorno)
            } // fine del ciclo for
            testoOut = testoRigaTitolo
        }// end of if

        return testoOut
    }// fine del metodo

    /**
     * Mappa dei turni <br>
     *
     * @param tipoTurno
     * @param dataInizio : giorno iniziale del periodo da considerare
     * @param dataFine :   giorno finale del periodo da considerare
     * @return mappaTurni
     *        key: giorno
     *        value: turno (eventualmente nullo)
     */
    private static LinkedHashMap<Date, Turno> creaMappaTurniDiUnTipo(
            TipoTurno tipoTurno,
            Date inizio,
            Date fine,
            int riga) {
        LinkedHashMap<Date, Turno> mappaTurniDiUnTipo = new LinkedHashMap<Date, Turno>()
        int giorni
        def giorno
        Turno turno
        def turni

        if (riga > 0) {
            riga--
        }// fine del blocco if

        if (inizio && fine) {
            giorni = fine - inizio

            for (int k = 0; k <= giorni; k++) {
                giorno = (inizio + k).toTimestamp()
                turni = Turno.findAllByTipoTurnoAndGiorno(tipoTurno, giorno, [sort: "id", order: "asc"])
                if (turni && turni.size() > riga) {
                    mappaTurniDiUnTipo.put(giorno, turni[riga])
                } else {
                    mappaTurniDiUnTipo.put(giorno, (Turno) null)
                }// fine del blocco if-else
            } // fine del ciclo for

        }// end of if

        return mappaTurniDiUnTipo
    }// fine del metodo

    /**
     * Lista dei turni <br>
     *
     * @param tipoTurno
     * @param dataInizio : giorno iniziale del periodo da considerare
     * @param dataFine :   giorno finale del periodo da considerare
     * @return array turni (eventualmente nullo)
     */
    private static ArrayList listaTurni(TipoTurno tipoTurno, Date inizio, Date fine) {
        ArrayList listaTurni = new ArrayList()
        int giorni
        def giorno
        Turno turno
        def turni

        if (inizio && fine) {
            giorni = fine - inizio

            for (int k = 0; k <= giorni; k++) {
                giorno = (inizio + k).toTimestamp()
                turni = Turno.findAllByTipoTurnoAndGiorno(tipoTurno, giorno, [sort: "id", order: "asc"])
                if (turni && turni.size() == 1) {
                    listaTurni.add(turni.get(0))
                } else {
                    listaTurni.add(null)
                }// fine del blocco if-else
            } // fine del ciclo for
        }// end of if

        return listaTurni
    }// fine del metodo

    //--per ogni funzione prevista, crea una riga
    //--la prima cella è il nome della funzione
    //--seguono sette celle di turno
    private static String rigaTurno(
            LinkedHashMap<Date,
                    Turno> mappaTurni,
            Funzione funzione,
            TipoTurno tipoTurno,
            boolean sempreCreabile,
            boolean militePuoCreareTurnoStandard,
            boolean militePuoCreareTurnoExtra,
            boolean usaNomeCognome) {
        String testoOut
        String testoRiga = ''
        Turno turno
        Date giorno

        //--la prima cella è il nome della funzione
        testoRiga += cellaFunzione(funzione)

        //--celle dei turni (7)
        if (mappaTurni) {
            mappaTurni.keySet().each {
                giorno = it
                turno = mappaTurni.get(it)
                testoRiga += cellaTurno(giorno, turno, funzione, tipoTurno, sempreCreabile, militePuoCreareTurnoStandard, militePuoCreareTurnoExtra, usaNomeCognome)
            }// end of each
        }// fine del blocco if

        testoOut = Lib.tagRiga(testoRiga)
        return testoOut
    }// fine del metodo

    //--singola cella del turno, vuoto o pieno
    private static String cellaTurno(
            Date giorno,
            Turno turno,
            Funzione funzioneDellaRiga,
            TipoTurno tipoTurno,
            boolean sempreCreabile,
            boolean militePuoCreareTurnoStandard,
            boolean militePuoCreareTurnoExtra,
            boolean usaNomeCognome) {
        String testoOut = ''
        String htmlPrefix = '/webambulanze/turno/'
        String htmlNew = htmlPrefix + 'newTurno'
        String htmlFill = htmlPrefix + 'fillTurno'
        String html
        String nome
        String nomeMilite
        Aspetto cella
        String testoVuoto = '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'
        testoVuoto += testoVuoto
        String testoCella = '<a href="http://designshack.net/" class="buttonamb">Click Me</a>'
        String testoCellaVuota = '<a href="http://designshack.net/" class="buttonamb">&nbsp;</a>'
        String tagTurno = 'turnoId'
        String tagAnno = 'anno'
        String tagGiorno = 'giorno'
        String tagTipoTurno = 'tipoTurno'
        String tagFunzione = 'funzione'
        String turnoId = ''
        String tipoTurnoTxt = ''
        String funzioneTxt = funzioneDellaRiga.sigla
        def giornoTxt = Lib.getNumGiorno(giorno)
        String annoTxt = Lib.getAnno(giorno)
        tipoTurnoTxt = tipoTurno.sigla
        Funzione funzioneDelTurno
        Milite milite
        int numFunzioni
        Aspetto aspetto

        if (turno == null) {
            html = htmlNew
            if (sempreCreabile) {
                cella = Aspetto.turnovuoto
            } else {
                cella = Aspetto.turnovuoto
            }// fine del blocco if-else
            nomeMilite = testoVuoto
        } else {
            numFunzioni = turno.tipoTurno.numFunzioni()
            html = htmlFill
            turnoId = turno.id
            if (turno.assegnato) {
                cella = Aspetto.turnoassegnato
                nomeMilite = testoVuoto
            } else {
                cella = Aspetto.turnoprevisto
                nomeMilite = testoVuoto
            }// fine del blocco if-else
        }// fine del blocco if-else

        aspetto = calcolaAspetto(turno, tipoTurno, sempreCreabile, militePuoCreareTurnoStandard, militePuoCreareTurnoExtra)

        if (numFunzioni) {
            for (int k = 1; k <= numFunzioni; k++) {
                nome = nomeMiliteCella(turno, funzioneDellaRiga, k, usaNomeCognome)
                if (nome) {
                    nomeMilite = nome
                }// fine del blocco if
            } // fine del ciclo for
        }// fine del blocco if

        testoCella = '<a href="'
        testoCella += html
        if (turno) {
            testoCella += "?${tagTurno}=${turnoId}"
        } else {
            testoCella += "?${tagAnno}=${annoTxt}&${tagGiorno}=${giornoTxt}&${tagTipoTurno}=${tipoTurnoTxt}"
        }// fine del blocco if-else
        testoCella += '">'
        testoCella += "<div>${nomeMilite}</div>"
        testoCella += '</a>'

        testoOut += Lib.tagCella(testoCella, aspetto)

        return testoOut
    }// fine del metodo

    private static Aspetto calcolaAspetto(
            Turno turno,
            TipoTurno tipoTurno,
            boolean sempreCreabile,
            boolean militePuoCreareTurnoStandard,
            boolean militePuoCreareTurnoExtra) {
        Aspetto aspetto
        Date giornoCorrente = Lib.creaDataOggi()
        Date giornoTurno
        int delta

        //--admin può sempre creare
        //--milite dipende dal tipo di turno e dai flag della croce
        if (sempreCreabile) {
            aspetto = Aspetto.turnovuoto
        } else {
            if (tipoTurno.multiplo) {
                if (militePuoCreareTurnoExtra) {
                    aspetto = Aspetto.turnovuoto
                } else {
                    aspetto = Aspetto.turnoeffettuato
                }// fine del blocco if-else
            } else {
                if (militePuoCreareTurnoStandard) {
                    aspetto = Aspetto.turnovuoto
                } else {
                    aspetto = Aspetto.turnoeffettuato
                }// fine del blocco if-else
            }// fine del blocco if-else
        }// fine del blocco if-else

        if (turno) {
            giornoTurno = turno.giorno
        }// fine del blocco if

        if (giornoTurno) {
            delta = giornoTurno - giornoCorrente

            if (delta < 1) {
                aspetto = Aspetto.turnoeffettuato
                return aspetto
            }// fine del blocco if

            if (turno.assegnato) {
                aspetto = Aspetto.turnoassegnato
                return aspetto
            }// fine del blocco if

            switch (delta) {
                case 1:
                    aspetto = Aspetto.turnocritico
                    break
                case 2:
                case 3:
                case 4:
                    aspetto = Aspetto.turnolibero
                    break
                default: // caso non definito
                    aspetto = Aspetto.turnoprevisto
                    break
            } // fine del blocco switch
        }// fine del blocco if

        return aspetto
    }// fine del metodo

    private
    static String nomeMiliteCella(Turno turno, Funzione funzioneDellaRiga, int numFunzione, boolean usaNomeCognome) {
        String nomeMilite = ''
        Milite milite
        Funzione funzioneDelTurno
        String nomeProprio
        boolean aggiungiNota = false
        String milFunz = 'militeFunzione' + numFunzione
        String funz = 'funzione' + numFunzione
        String nota = 'problemiFunzione' + numFunzione
        String tagNota = '<font color="#FF0000"> *</font>'
        String notaTxt

        if (turno."${nota}") {
            notaTxt = turno."${nota}"
            if (notaTxt && notaTxt.equals('true')) {
                aggiungiNota = true
            }// fine del blocco if

        }// fine del blocco if

        if (turno && turno."${milFunz}") {
            milite = turno."${milFunz}"
            if (milite && milite.cognome) {
                if (turno."${funz}") {
                    funzioneDelTurno = turno."${funz}"
                    if (funzioneDelTurno == funzioneDellaRiga) {
                        if (usaNomeCognome) {
                            nomeMilite = milite.getNomeCognomePuntato()
                        } else {
                            nomeMilite = milite.getCognomeNomePuntato()
                        }// fine del blocco if-else
                        if (aggiungiNota) {
                            nomeMilite = tagNota + nomeMilite
                            //nomeMilite = '<font color="#FF6666">' + nomeMilite + '</font>'
                        }// fine del blocco if
                    }// fine del blocco if
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return nomeMilite
    }// fine del metodo

    /**
     * Riga orario oppure località <br>
     *
     * @param tipoTurno
     * @param funzione
     * @return testo del tag
     */
    private static String rigaOrario(int giorni, TipoTurno tipoTurno, int span, int pariDispari) {
        String testoOut
        String testoRiga = ''
        Date giorno
        Turno turno

        testoRiga += cellaTipoTurno(tipoTurno, span, pariDispari)
        testoRiga += cellaOrario(tipoTurno)

        //--celle dei giorni (7)
        for (int k = 0; k < giorni; k++) {
            testoRiga += Lib.tagCella('', Aspetto.turnoorario)
        } // fine del ciclo for
        testoOut = Lib.tagRiga(testoRiga)

        return testoOut
    }// fine del metodo

    /**
     * Prima colonna <br>
     */
    private static String cellaTipoTurno(TipoTurno tipoTurno, int span, int pariDispari) {
        String testoOut
        String testo

        testo = tipoTurno.descrizione
        testo = Lib.primaMaiuscola(testo)

        if (pariDispari % 2 == 0) {
            testoOut = Lib.tagCella(testo, Aspetto.legendatipoodd, span)
        } else {
            testoOut = Lib.tagCella(testo, Aspetto.legendatipoeven, span)
        }// fine del blocco if-else

        return testoOut
    }// fine del metodo

    /**
     * Seconda colonna <br>
     */
    private static String cellaOrario(TipoTurno tipoTurno) {
        String testoOut
        String testoInizio
        String testoFine
        String testo
        boolean mostraOrario = true

        if (tipoTurno && tipoTurno.orario) {
            if (mostraOrario) {
                testoInizio = tipoTurno.oraInizio
                if (tipoTurno.minutiInizio && tipoTurno.minutiInizio > 0) {
                    testoInizio += ':' + tipoTurno.minutiInizio
                }// fine del blocco if
                testoFine = tipoTurno.oraFine
                if (tipoTurno.minutiFine && tipoTurno.minutiFine > 0) {
                    testoFine += ':' + tipoTurno.minutiFine
                }// fine del blocco if
                testo = testoInizio + '-' + testoFine
            } else {
                testo = '(orario)'
            }// fine del blocco if-else
        } else {
            testo = '(località)'
        }// fine del blocco if-else

        testoOut = Lib.tagCella(testo, Aspetto.legendaorario)

        return testoOut
    }// fine del metodo

    /**
     * Seconda colonna <br>
     */
    private static String cellaFunzione(Funzione funzione) {
        String testoOut = ''
        String testo = ''
        boolean parentesi = false

        if (funzione) {
            if (parentesi) {
                testo += '('
            }// fine del blocco if
            testo += funzione.siglaVisibile
            if (parentesi) {
                testo += ')'
            }// fine del blocco if
            testoOut = Lib.tagCella(testo, Aspetto.legendafunzione)
        }// fine del blocco if

        return testoOut
    }// fine del metodo

    /**
     * Legenda in fondo <br>
     *
     * @return testo del tag
     */
    private static String legenda() {
        String testo = ''

        testo += Lib.tagCella('Legenda', Aspetto.footerlegenda)
        testo += Lib.tagCella('', Aspetto.turnoeffettuato)
        testo += Lib.tagCella('Turno effettuato o non creabile', Aspetto.footercella)
//        testo += Lib.tagCella('', Aspetto.turnobloccato)
//        testo += Lib.tagCella('Turno assegnato bloccato e non più modificabile')
        testo += Lib.tagCella('', Aspetto.turnocritico)
        testo += Lib.tagCella('Turno critico da assegnare subito', Aspetto.footercella)
        testo += Lib.tagCella('', Aspetto.turnolibero)
        testo += Lib.tagCella('Turno da assegnare nei prossimi giorni', Aspetto.footercella)
        testo += Lib.tagCella('', Aspetto.turnoassegnato)
        testo += Lib.tagCella('Turno assegnato normale (funzioni obbligatorie coperte)', Aspetto.footercella)
        testo += Lib.tagCella('', Aspetto.turnoprevisto)
        testo += Lib.tagCella('Turno previsto e non ancora completamente assegnato', Aspetto.footercella)
        testo += Lib.tagCella('', Aspetto.turnovuoto)
        testo += Lib.tagCella('Turno creabile', Aspetto.footercella)

        testo = Lib.tagRiga(testo)
        testo = Lib.tagTable(testo)
        return testo
    }// fine del metodo

    /**
     * Pagina di selezione del tipo di viaggio<br>
     *
     * @return testo del tag
     */
    def selezioneViaggio = {
        String testoOut = ''

        testoOut += Lib.tagController('Viaggio', TipoViaggio.auto118.nome, 'nuovo118')
        testoOut += Lib.tagController('Viaggio', TipoViaggio.ordinario.nome, 'nuovoOrdinario')
        testoOut += Lib.tagController('Viaggio', TipoViaggio.dializzati.nome, 'nuovoDializzati')
        testoOut += Lib.tagController('Viaggio', TipoViaggio.interno.nome, 'nuovoInterno')

        out << testoOut
    }// fine della closure

    /**
     * Copyright e/o versione in fondo <br>
     *
     * @return testo del tag
     */
    private static String copyright() {
        String testoOut
        String testo

//        testo = 'Algos© - v3.4 del 27 giugno 2013'
//        testo = 'Algos© - v3.5 del 30 giugno 2013'
//        testo = 'Algos© - v3.6 del 30 giugno 2013'
//        testo = 'Algos© - v3.7 del 1 luglio 2013'
//        testo = 'Algos© - v3.8 del 12 luglio 2013'
//        testo = 'Algos© - v3.9 del 30 luglio 2013'
//        testo = 'Algos© - v4.0 del 2 agosto 2013'
//        testo = 'Algos© - v4.2 del 15 ottobre 2013'
//        testo = 'Algos© - v4.3 del 26 novembre 2013'
//        testo = 'Algos© - v4.4 del 30 novembre 2013'
//        testo = 'Algos© - v4.5 del 17 dicembre 2013'
//        testo = 'Algos© - v4.6 del 18 dicembre 2013'
//        testo = 'Algos© - v4.7 del 21 dicembre 2013'
//        testo = 'Algos© - v4.8 del 26 dicembre 2013'
//        testo = 'Algos© - v4.9 del 27 dicembre 2013'
//        testo = 'Algos© - v5.0 del 7 gennaio 2014'
//        testo = 'Algos© - v5.1 del 8 gennaio 2014'
//        testo = 'Algos© - v5.2 del 13 gennaio 2014'
//        testo = 'Algos© - v5.3 del 20 maggio 2014'
//        testo = 'Algos© - 5.6 del 29 luglio 2014'
//        testo = 'Algos© - 5.10 del 12 gennaio 2015'
//        testo = 'Algos© - 5.11 del 16 gennaio 2015'
//        testo = 'Algos© - 5.12 del 27 gennaio 2015'
//        testo = 'Algos© - 5.14 del 7 febbraio 2015'
//        testo = 'Algos© - 5.15 del 10 febbraio 2015'
//        testo = 'Algos© - 5.17 del 11 febbraio 2015'
//        testo = 'Algos© - 5.18 del 3 aprile 2015'
//        testo = 'Algos© - 5.19 del 5 maggio 2015'
//        testo = 'Algos© - 5.20 del 11 maggio 2015'
//        testo = 'Algos© - 5.21 del 23 maggio 2015'
//        testo = 'Algos© - 5.22 del 3 dicembre 2015'
//        testo = 'Algos© - 5.23 del 19 settembre 2017'
//        testo = 'Algos© - 5.24 del 24 settembre 2017'
//        testo = 'Algos© - 5.25 del 22 dicembre 2017'
//        testo = 'Algos© - 5.26 del 29 gennaio 2018'
//        testo = 'Algos© - 5.27 del 7 febbraio 2018'
//        testo = 'Algos© - 5.28 del 15 febbraio 2018'
//        testo = 'Algos© - 5.29 del 19 marzo 2018'
//        testo = 'Algos© - 5.30 del 4 febbraio 2019'
//        testo = 'Algos© - 5.31 del 3 febbraio 2019'
        testo = 'Algos© - 5.32 del 3 dicembre 2019'
        testo = Lib.tagCella(testo, Aspetto.copyright)
        testoOut = Lib.tagTable(testo)
        return testoOut
    }// fine del metodo

    /**
     * Form del singolo turno <br>
     *
     * @param mappa parametri
     *        dataInizio: giorno iniziale del periodo da considerare
     *        dataFine:   giorno finale del periodo da considerare
     * @return testo del tag
     */
    def fillForm = { mappa ->
        String testoOut = ''
        Croce croce = null
        String testoRiga = ''
        Turno turno = null
        String turnoIdTxt
        long turnoId
        int numFunzioni
        TipoTurno tipoTurno
        String nuovoTurnoTxt
        boolean nuovoTurno = false
        boolean mostraOreSingoloMilite = false
        boolean isTurnoExtra = false
        boolean isTurnoSettimanale = false
        boolean inserimento = true //true se admin oppure se milite non è già segnato nel turno

        if (params.siglaCroce) {
            isTurnoSettimanale = croceService.isTurnoSettimanale(params.siglaCroce)
            croce = Croce.findBySigla((String) params.siglaCroce)
        }// fine del blocco if
        if (mappa.turnoInstance) {
            turnoIdTxt = mappa.turnoInstance
            turnoId = Long.decode(turnoIdTxt)
            turno = Turno.get(turnoId)
        }// fine del blocco if
        if (mappa.nuovoTurno) {
            nuovoTurnoTxt = mappa.nuovoTurno
            if (nuovoTurnoTxt && nuovoTurnoTxt.equals('true')) {
                nuovoTurno = true
                mostraOreSingoloMilite = true
            }// fine del blocco if
        }// fine del blocco if

        if (turno.militeFunzione1 == null && turno.militeFunzione2 == null && turno.militeFunzione3 == null) {
            mostraOreSingoloMilite = true
        }// fine del blocco if

        if (turno) {
            tipoTurno = turno.tipoTurno
            if (tipoTurno.sigla.equals(Cost.EXTRA)) {
                isTurnoExtra = true
            }// fine del blocco if

            numFunzioni = tipoTurno.numFunzioni()
            testoOut += formCaption(turno)

            testoOut += formRiga('Giorno', Lib.presentaDataCompleta(turno.giorno))
            testoOut += formRiga('Tipo di turno', formTipoTurno(turno))
            testoOut += formRiga('Inizio', formData(croce, tipoTurno, turno.inizio, 'inizio'))
            testoOut += formLegenda('Orario di inizio turno (eventualmente modificabile)')
            testoOut += formRiga('Fine', formData(croce, tipoTurno, turno.fine, 'fine'))
            testoOut += formLegenda('Orario di fine turno (eventualmente modificabile)')
            if (isTurnoExtra) {
//                testoOut += formRiga('Titolo', Lib.tagCella('mario'))
//                testoOut += formRiga('Località', Lib.presentaDataCompleta(turno.giorno))
            }// fine del blocco if
            for (int k = 1; k <= numFunzioni; k++) {
                testoOut += formRigaFunzione(turno, mostraOreSingoloMilite, k)
                testoOut += formLegenda('Lista (in ordine alfabetico) di tutti i militi')
            } // fine del ciclo for
            testoOut += formRiga('Note', formNote(turno.note))
            if (isTurnoSettimanale && inserimento) {
                testoOut += getRipetizioneTurno(turno)
            }// fine del blocco if

        }// fine del blocco if

        testoOut = Lib.tagTable(testoOut, Aspetto.formtable)
        out << testoOut
    }// fine della closure

    //--presenta la riga di ripetizione settimanale del turno
    //--se è admin sempre (in update controlla se sia valido l'inserimento, qui è solo presentato)
    //--solo se il milite non è già segnato
    //--solo se ci sono funzioni ancora disponibili
    //--solo se il milite è abilitato per le funzioni disponibili
    //--solo se il turno non è bloccato (storico, scaduto o imminente)
    //--sempre nella creazione di un nuovo turno
    private static String getRipetizioneTurno(Turno turno) {
        String testoOut = ''
        boolean inserimento = true //true se admin oppure se milite non è già segnato nel turno
        String inputTxt = ''
        String testoUno
        String testoDue
        ArrayList valori = Cost.VALORI_FREQUENZA_TXT
        String label = Cost.CAMPO_NUMERO_RIPETIZIONI
        int numSuggerito = 2

        testoUno = formRadio(Cost.CAMPO_RIPETIZIONE_TURNO, 'Vuoi ripetere il turno settimanale?', false)
        testoUno = Lib.tag('td', testoUno, Aspetto.formlabelleft.toString(), 'col', 1)
        testoDue = formSelect(Cost.CAMPO_FREQUENZA_RIPETIZIONE, valori, 1)
        testoDue += Lib.tagCella('per', Aspetto.formlabelleft)
        inputTxt += "<input type=\"text\" name="
        inputTxt += label
        inputTxt += " value="
        inputTxt += numSuggerito
        inputTxt += " style=\"width: 30px\" id=\"${label}\"/>"
        testoDue += Lib.tagCella(inputTxt, Aspetto.formlabelleft)
        testoDue += Lib.tagCella('volte', Aspetto.formlabelleft)
        testoDue = Lib.tag('td', testoDue, Aspetto.formeditleft.toString(), 'col', 1)

        testoOut += Lib.tagRiga(testoUno + testoDue)
        return testoOut
    }// fine del metodo

    /**
     * Form del singolo viaggio <br>
     *
     * @return testo del tag
     */
    def newViaggio = { mappa ->
        String testoOut = ''
        Croce croce = null
        String descrizioneTurno = 'turno non specificato'
        Automezzo mezzo = null
        String targaMezzo = 'mezzo non specificato'
        long mezzoId = 0
        long chilometriPartenza = 0
        long chilometriArrivo = 0
        String testoRiga
        Turno turno = null
        String turnoIdTxt
        long turnoId = 0
        int numFunzioni
        TipoTurno tipoTurno
        String nuovoTurnoTxt
        boolean nuovoTurno = false
        boolean mostraOreSingoloMilite = false
        boolean isTurnoExtra = false
        def a
        def b
        Date oggi = new Date()
        Date giorno
        ArrayList listaInvio = CodiceInvio.getAll()
        ArrayList listaLuogo = LuogoEvento.getAll()
        ArrayList listaPatologia = Patologia.getAll()
        ArrayList listaRicovero = CodiceRicovero.getAll()
        int numeroServiziEffettuati = 0
        int numeroViaggio = 0
        boolean usaListaMilitiViaggi = false

        if (params.siglaCroce) {
            croce = Croce.findBySigla((String) params.siglaCroce)
            numeroServiziEffettuati = croceService.getNumeroServiziEffettuati(croce) + 1
            usaListaMilitiViaggi = croceService.usaListaMilitiViaggi(croce.sigla)
        }// fine del blocco if
        if (mappa.tipoViaggio) {
            a = mappa.tipoViaggio
        }// fine del blocco if
        if (mappa.automezzoId) {
            mezzo = Automezzo.findById(mappa.automezzoId)
            if (mezzo) {
                mezzoId = mezzo.id
                targaMezzo = mezzo.targa
                chilometriPartenza = mezzo.chilometriTotaliPercorsi
                numeroViaggio = mezzo.numeroViaggiEffettuati + 1
            }// fine del blocco if
        }// fine del blocco if

        if (mappa.turnoId) {
            turno = Turno.get(mappa.turnoId)
            if (turno) {
                turnoId = turno.id
            }// fine del blocco if
        }// fine del blocco if

        if (turno) {
            giorno = turno.giorno
            tipoTurno = turno.tipoTurno
            if (tipoTurno) {
                descrizioneTurno = tipoTurno.descrizione
            }// fine del blocco if

            testoOut += LibHtml.field(Field.txtLink, 'giorno', Lib.presentaDataCompleta(giorno), "turno/fillTurno?turnoId=${turnoId}")
            testoOut += LibHtml.field(Field.txtLink, 'turno', descrizioneTurno, "turno/fillTurno?turnoId=${turnoId}")
            testoOut += LibHtml.field(Field.txtLink, 'automezzo utilizzato', targaMezzo, "automezzo/show/${mezzoId}")
            testoOut += LibHtml.field(Field.txtLink, 'chilometri alla partenza', chilometriPartenza, "automezzo/show/${mezzoId}")
            testoOut += LibHtml.field(Field.txtObbEdit, "chilometri all'arrivo", '', 'chilometriArrivo')
            testoOut += LibHtml.field(Field.oraMin, "orario di chiamata", oggi, 'inizio')
            testoOut += LibHtml.fieldLista("codice invio", 'codiceInvio', listaInvio, CodiceInvio.get(), true)
            testoOut += LibHtml.fieldLista("luogo evento", 'luogoEvento', listaLuogo, LuogoEvento.get(), true)
            testoOut += LibHtml.fieldLista("patologia segnalata", 'patologia', listaPatologia, Patologia.get(), true)
            testoOut += LibHtml.fieldLista("codice ricovero", 'codiceRicovero', listaRicovero, CodiceRicovero.get(), true)
            testoOut += LibHtml.field(Field.oraMin, "orario di rientro", oggi, 'rientro')
            testoOut += LibHtml.field(Field.txtObbEdit, "Cartellino 118", '', 'numeroCartellino')
            testoOut += LibHtml.field('nomePaziente')
            testoOut += LibHtml.field('indirizzoPaziente')
            testoOut += LibHtml.field('cittaPaziente')
            testoOut += LibHtml.field('etaPaziente')
            testoOut += LibHtml.field('prelievo')
            testoOut += LibHtml.field('ricovero')
            testoOut += LibHtml.field(Field.txtObbEdit, 'numeroBolla')
            testoOut += LibHtml.field(Field.txtObbEdit, 'numero servizio globale', numeroServiziEffettuati, 'numeroServizio')
            testoOut += LibHtml.field(Field.txtObbEdit, 'numero viaggio del mezzo', numeroViaggio, 'numeroViaggio')
            testoOut += listaMilitiFunzioni(turno, usaListaMilitiViaggi)
        }// fine del blocco if

        out << testoOut
    }// fine della closure

    def editViaggio = {
        String testoOut = ''
        Croce croce
        Viaggio viaggio = null
        Date giorno
        Date inizio
        Date fine
        Turno turno
        Automezzo automezzo
        ArrayList listaAutomezzi
        int chilometriPartenza
        int chilometriArrivo
        CodiceInvio codiceInvio
        ArrayList listaInvio = CodiceInvio.getAll()
        LuogoEvento luogoEvento
        ArrayList listaLuogo = LuogoEvento.getAll()
        Patologia patologia
        ArrayList listaPatologia = Patologia.getAll()
        CodiceRicovero codiceRicovero
        ArrayList listaRicovero = CodiceRicovero.getAll()
        String numeroCartellino
        String nomePaziente
        String indirizzoPaziente
        String cittaPaziente
        String etaPaziente
        String prelievo
        String ricovero
        int numeroBolla
        int numeroServizio
        int numeroViaggio
        ArrayList<Milite> listaMiliti = new ArrayList()

        if (params.id) {
            viaggio = Viaggio.findById(params.id)
        }// fine del blocco if

        if (!viaggio) {
            out << 'Qualcosa non ha funzionato'
            return
        }// fine del blocco if

        croce = viaggio.croce
        turno = viaggio.turno
        listaAutomezzi = Automezzo.findAllByCroce(croce)
        giorno = viaggio.giorno
        inizio = viaggio.inizio
        fine = viaggio.fine
        automezzo = viaggio.automezzo
        chilometriPartenza = viaggio.chilometriPartenza
        chilometriArrivo = viaggio.chilometriArrivo
        codiceInvio = viaggio.codiceInvio
        luogoEvento = viaggio.luogoEvento
        patologia = viaggio.patologia
        codiceRicovero = viaggio.codiceRicovero
        numeroCartellino = viaggio.numeroCartellino
        nomePaziente = viaggio.nomePaziente
        indirizzoPaziente = viaggio.indirizzoPaziente
        cittaPaziente = viaggio.cittaPaziente
        etaPaziente = viaggio.etaPaziente
        prelievo = viaggio.prelievo
        ricovero = viaggio.ricovero
        numeroBolla = viaggio.numeroBolla
        numeroServizio = viaggio.numeroServizio
        numeroViaggio = viaggio.numeroViaggio
        if (viaggio.militeFunzione1) {
            listaMiliti.add(viaggio.militeFunzione1)
        } else {
            listaMiliti.add(null)
        }// fine del blocco if-else
        if (viaggio.militeFunzione2) {
            listaMiliti.add(viaggio.militeFunzione2)
        } else {
            listaMiliti.add(null)
        }// fine del blocco if-else
        if (viaggio.militeFunzione3) {
            listaMiliti.add(viaggio.militeFunzione3)
        } else {
            listaMiliti.add(null)
        }// fine del blocco if-else
        if (viaggio.militeFunzione4) {
            listaMiliti.add(viaggio.militeFunzione4)
        } else {
            listaMiliti.add(null)
        }// fine del blocco if-else

        testoOut += LibHtml.field('Giorno', formDataGiornoEdit(giorno, 'giorno'))
        testoOut += LibHtml.fieldLista('Automezzo', 'automezzo', listaAutomezzi, automezzo.toString(), true)
        testoOut += LibHtml.field(Field.txtObbEdit, 'Chilometri alla partenza', chilometriPartenza, 'chilometriPartenza')
        testoOut += LibHtml.field(Field.txtObbEdit, "Chilometri all'arrivo", chilometriArrivo, 'chilometriArrivo')
        testoOut += LibHtml.field(Field.oraMin, "Orario di chiamata", inizio, 'inizio')
        testoOut += LibHtml.fieldLista("Codice invio", 'codiceInvio', listaInvio, codiceInvio.toString(), true)
        testoOut += LibHtml.fieldLista("Luogo evento", 'luogoEvento', listaLuogo, luogoEvento.toString(), true)
        testoOut += LibHtml.fieldLista("Patologia segnalata", 'patologia', listaPatologia, patologia.toString(), true)
        testoOut += LibHtml.fieldLista("Codice ricovero", 'codiceRicovero', listaRicovero, codiceRicovero.toString(), true)
        testoOut += LibHtml.field(Field.oraMin, "Orario di rientro", fine, 'rientro')
        testoOut += LibHtml.field(Field.txtObbEdit, "Cartellino 118", numeroCartellino, 'numeroCartellino')
        testoOut += LibHtml.field(Field.txtEdit, 'Nome del paziente', nomePaziente, 'nomePaziente')
        testoOut += LibHtml.field(Field.txtEdit, 'Indirizzo del paziente', indirizzoPaziente, 'indirizzoPaziente')
        testoOut += LibHtml.field(Field.txtEdit, 'Città del paziente', cittaPaziente, 'cittaPaziente')
        testoOut += LibHtml.field(Field.txtEdit, 'Età del paziente', etaPaziente, 'etaPaziente')
        testoOut += LibHtml.field(Field.txtEdit, 'Località prelievo', prelievo, 'prelievo')
        testoOut += LibHtml.field(Field.txtEdit, 'Località ricovero', ricovero, 'ricovero')
        testoOut += LibHtml.field(Field.txtObbEdit, 'Numero della bolla', numeroBolla, 'numeroBolla')
        testoOut += LibHtml.field(Field.txtObbEdit, 'Numero del servizio', numeroServizio, 'numeroServizio')
        testoOut += LibHtml.field(Field.txtObbEdit, 'Numero del viaggio', numeroViaggio, 'numeroViaggio')
        testoOut += listaMilitiFunzioni(turno, listaMiliti)

        out << testoOut
    }// fine della closure

    def editGiorno = {
        String testoOut = ''
        Date giorno = new Date()

        if (params.giorno) {
            giorno = params.giorno
        }// fine del bloc

        testoOut += LibHtml.field('Giorno', formDataGiornoAnnoEdit(giorno, 'giorno'))

        out << testoOut
    }// fine della closure


    private String listaMilitiFunzioni(Turno turno, boolean usaListaMilitiViaggi) {
        String testoOut = ''
        int max = 4

        for (int k = 1; k <= max; k++) {
            testoOut += listaMiliteFunzioni(turno, usaListaMilitiViaggi, k)
        } // fine del ciclo for

        return testoOut
    }// fine del metodo

    private String listaMiliteFunzioni(Turno turno, boolean usaListaMilitiViaggi, int pos) {
        String testoOut = ''
        Funzione funz
        Milite milite = getMiliteForFunzione(turno, pos)

        if (usaListaMilitiViaggi) {
            funz = getFunzione(turno, pos)
            if (funz) {
                testoOut += listaMiliti(funz, milite, pos)
            }// fine del blocco if
        } else {
            funz = getFunzione(turno, pos)
            if (funz) {
                if (milite) {
                    testoOut += LibHtml.field(Field.txtEdit, funz.descrizione, milite.cognomeNome, "")
                } else {
                    testoOut += LibHtml.field(Field.txtEdit, funz.descrizione, '...', "")
                }// fine del blocco if-else
            }// fine del blocco if
        }// fine del blocco if-else

        return testoOut
    }// fine del metodo


    private String listaMiliti(Funzione funz, Milite milite, int numFunzione) {
        String testoOut
        Croce croce
        String label = ''
        ArrayList lista = null
        String campo = 'militeFunzione' + numFunzione

//        if (turno."${num}") {
//            milite = turno."${campo}"
//        }// fine del blocco if

        if (funz) {
            croce = funz.croce
            label = funz.descrizione
        }// fine del blocco if

        if (croce && militeService) {
            lista = militeService.listaMilitiAbilitati(croce, funz)
        }// fine del blocco if

//        testoOut = LibHtml.field(label, milite.toString())
        testoOut = LibHtml.fieldLista(label, campo, lista, milite, false)

        return testoOut
    }// fine del metodo

    private String listaMilitiFunzioni(Turno turno, ArrayList<Milite> listaDeiMiliti) {
        String testoOut = ''
        Funzione funz
        int max = 4
        Milite milite

        for (int k = 1; k <= max; k++) {
            funz = getFunzione(turno, k)
            if (funz) {
                milite = (Milite) listaDeiMiliti.get(k - 1)
                testoOut += listaMiliti(funz, milite, k)
            }// fine del blocco if
        } // fine del ciclo for

        return testoOut
    }// fine del metodo

    private static String formTipoTurno(Turno turno) {
        String testo = ''
        TipoTurno tipoTurno
        int durata

        if (turno) {
            tipoTurno = turno.tipoTurno
        }// fine del blocco if

        if (tipoTurno) {
            testo = Lib.primaMaiuscola(tipoTurno.descrizione)
            durata = tipoTurno.durata
            testo += ' (' + durata + ' ore)'
        }// fine del blocco if

        return testo
    }// fine del metodo

    private static String formRiga(String label, String value) {
        String testoOut = ''
        String testoRiga = ''

        testoRiga += Lib.tagCella(label, Aspetto.formlabelleft)
        testoRiga += Lib.tag('td', value, Aspetto.formeditleft.toString(), 'col', 3)
        testoOut += Lib.tagRiga(testoRiga)

        return testoOut
    }// fine del metodo

    private static String formRigaTab(String label, String value) {
        String testoOut = ''
        String testoRiga = ''

        testoOut += '<div class="fieldcontain">'
        testoOut += Lib.tagCella(label, Aspetto.formlabelleft)
        testoOut += Lib.tag('td', value, Aspetto.formeditleft.toString(), 'col', 3)
        testoOut += '</div>'

        return testoOut
    }// fine del metodo

    private String formRigaFunzione(Turno turno, boolean mostraOreSingoloMilite, int riga) {
        String testoOut = ''
        String testoRiga = ''

        testoRiga += Lib.tagCella(formFunzioneLabel(turno, riga), Aspetto.formlabelleft)
        testoRiga += Lib.tagCella(formFunzioneEdit(turno, riga), Aspetto.formeditleft)
        testoRiga += Lib.tagCella(formBox(turno, riga), Aspetto.formlabelright)
        testoRiga += Lib.tagCella('ore', Aspetto.formlabelright)
        testoRiga += Lib.tagCella(formOreMilite(turno, mostraOreSingoloMilite, riga), Aspetto.formeditright)
        testoOut += Lib.tagRiga(testoRiga)

        return testoOut
    }// fine del metodo


    private static String formLegenda(String legenda) {
        String testoOut = ''
        String testoRiga = ''
        boolean usaLegenda = false
        int col = 4

        // testoRiga += Lib.tagCella(legenda, Aspetto.formlegenda, col)
        testoRiga = Lib.tag('td', legenda, Aspetto.formlegenda.toString(), 'col', col)

        if (usaLegenda) {
            testoOut += Lib.tagRiga(testoRiga)
        }// fine del blocco if

        return testoOut
    }// fine del metodo


    private String formData(Croce croce, TipoTurno tipoTurno, Date data, String iniziofine) {
        String testoOut
        boolean isExtra = false
        boolean isOrarioTurnoModificabileForm = croceService.isOrarioTurnoModificabileForm(croce)

        if (tipoTurno) {
            isExtra = !tipoTurno.orario
        }// fine del blocco if

        if (isOrarioTurnoModificabileForm || isExtra) {
            testoOut = formDataEdit(data, iniziofine)
        } else {
            testoOut = formDataShow(data)
        }// fine del blocco if-else

        return testoOut
    }// fine del metodo

    private static String formDataEdit(Date data, String iniziofine) {
        String testoOut = ''
        int numOra = Lib.getNumOra(data)
        int numMinuti = Lib.getNumMinuti(data)
        int numGiorno = Lib.getNumGiornoMese(data)
        int quantiGiorniNelMese = Lib.getNumGiorniNelMese(data)
        int numMese = Lib.getNumMese(data)

        testoOut += formInput(data, iniziofine)
        testoOut += formInputStruct(iniziofine)
        testoOut += formSelectHour(iniziofine, numOra)
        testoOut += ' : '
        testoOut += formSelectMinute(iniziofine, numMinuti)
        testoOut += ' del '
        testoOut += formSelectGiorno(iniziofine, numGiorno, quantiGiorniNelMese)
        testoOut += formSelectMese(iniziofine, numMese)

        return testoOut
    }// fine del metodo

    private static String formDataGiornoEdit(Date data, String iniziofine) {
        String testoOut = ''
        int numOra = Lib.getNumOra(data)
        int numMinuti = Lib.getNumMinuti(data)
        int numGiorno = Lib.getNumGiornoMese(data)
        int quantiGiorniNelMese = Lib.getNumGiorniNelMese(data)
        int numMese = Lib.getNumMese(data)

        testoOut += formInput(data, iniziofine)
        testoOut += formInputStruct(iniziofine)
        testoOut += formSelectGiorno(iniziofine, numGiorno, quantiGiorniNelMese)
        testoOut += formSelectMese(iniziofine, numMese)

        return testoOut
    }// fine del metodo

    private static String formDataGiornoAnnoEdit(Date data, String iniziofine) {
        String testoOut = ''
        int numGiorno = Lib.getNumGiornoMese(data)
        int quantiGiorniNelMese = Lib.getNumGiorniNelMese(data)
        int numMese = Lib.getNumMese(data)
        ArrayList anni = Cost.ANNI

        testoOut += formInput(data, iniziofine)
        testoOut += formInputStruct(iniziofine)
        testoOut += formSelectGiorno(iniziofine, numGiorno, quantiGiorniNelMese)
        testoOut += formSelectMese(iniziofine, numMese)
        testoOut += formSelect('anno', anni, 3)

        return testoOut
    }// fine del metodo

    private static String formDataShow(Date data) {
        return Lib.presentaOraData(data)
    }// fine del metodo

    private static String formInput(Date data, String inizioFine) {
        String testo = ''
        String anno = Lib.getAnno(data)

        testo += "<input type=\"hidden\" name=\"${inizioFine}_year\" value=\"${anno}\" id=\"${inizioFine}_year\">\n"

        return testo
    }// fine del metodo

    private static String formInputStruct(String inizioFine) {
        return "<input type=\"hidden\" name=\"${inizioFine}\" value=\"date.struct\"/>\n"
    }// fine del metodo

    private static String formSelectHour(String inizioFine, int selected) {
        String testo = ''
        int max = 24

        testo += "<select name=\"${inizioFine}_hour\" id=\"${inizioFine}_hour\">\n"

        for (int k = 0; k < max; k++) {
            if (k == selected) {
                testo += "<option value=\"${k}\" selected=\"selected\">${k}</option>\n"
            } else {
                testo += "<option value=\"${k}\">${k}</option>\n"
            }// fine del blocco if-else
        } // fine del ciclo for
        testo += '</select>\n'

        return testo
    }// fine del metodo

    private static String formSelectMinute(String inizioFine, int selected) {
        String testo = ''
        int max = 45

        testo += "<select name=\"${inizioFine}_minute\" id=\"${inizioFine}_minute\">\n"

        for (int k = 0; k <= max; k += 15) {
            if (k == selected) {
                testo += "<option value=\"${k}\" selected=\"selected\">${k}</option>\n"
            } else {
                testo += "<option value=\"${k}\">${k}</option>\n"
            }// fine del blocco if-else
        } // fine del ciclo for
        testo += '</select>\n'

        return testo
    }// fine del metodo

    private static String formSelectGiorno(String inizioFine, int selected, int quantiGiorniNelMese) {
        String testo = ''

        testo += "<select name=\"${inizioFine}_day\" id=\"${inizioFine}_day\">\n"

        for (int k = 0; k <= quantiGiorniNelMese; k++) {
            if (k == selected) {
                testo += "<option value=\"${k}\" selected=\"selected\">${k}</option>\n"
            } else {
                testo += "<option value=\"${k}\">${k}</option>\n"
            }// fine del blocco if-else
        } // fine del ciclo for
        testo += '</select>\n'

        return testo
    }// fine del metodo

    private static String formSelectMese(String inizioFine, int selected) {
        String testo = ''
        int max = 12
        String mese
        testo += "<select name=\"${inizioFine}_month\" id=\"${inizioFine}_month\">\n"

        for (int k = 1; k <= max; k++) {
            mese = Mese.getLong(k)
            if (k == selected) {
                testo += "<option value=\"${k}\" selected=\"selected\">${mese}</option>\n"
            } else {
                testo += "<option value=\"${k}\">${mese}</option>\n"
            }// fine del blocco if-else
        } // fine del ciclo for
        testo += '</select>\n'

        return testo
    }// fine del metodo

    private static String formSelect(String nome, ArrayList valori) {
        return formSelect(nome, valori, 0)
    }// fine del metodo

    private static String formSelect(String nome, ArrayList valori, int selected) {
        String testo = ''
        int max = valori.size()
        String valore
        testo += "<select name=\"${nome}\" id=\"${nome}\">\n"

        for (int k = 1; k <= max; k++) {
            valore = valori.get(k - 1)
            if (k == selected) {
                testo += "<option value=\"${k}\" selected=\"selected\">${valore}</option>\n"
            } else {
                testo += "<option value=\"${k}\">${valore}</option>\n"
            }// fine del blocco if-else
        } // fine del ciclo for
        testo += '</select>\n'

        return testo
    }// fine del metodo

    private static String formFunzioneLabel(Turno turno, int riga) {
        String label = ''
        TipoTurno tipoTurno = turno.tipoTurno
        ArrayList funzioni
        Funzione funzione

        if (riga > 0) {
            riga--
        }// fine del blocco if

        if (tipoTurno) {
            funzioni = tipoTurno.getListaFunzioni()
            if (funzioni) {
                funzione = (Funzione) funzioni[riga]
                if (funzione) {
                    label = funzione.descrizione
                }// fine del blocco if
            }// fine del blocco if
        }// fine del blocco if

        return label
    }// fine del metodo

    //--selettore per il nome del milite
    //--a seconda del flag generale e del ruolo con cui si è loggati, mostra:
    //--1) tutti i militi
    //--2) solo il milite loggato
    private String formFunzioneEdit(Turno turno, int riga) {
        String testo = ''
        Croce croce = turno.croce
        boolean isAdmin = militeService.isLoggatoAdminOrMore()
        boolean isPuoInserireAltri = croceService.isMilitePuoInserireAltri(croce)
        Funzione funzione = getFunzione(turno, riga)

        testo += "<select name=\"militeFunzione${riga}_id\" id=\"militeFunzione${riga}_id\">\n"

        if (isAdmin || isPuoInserireAltri) {
            testo += formFunzioneEditAll(turno, riga)
        } else {
            testo += formFunzioneEditLoggato(turno, riga, funzione)
        }// fine del blocco if-else

        testo += '</select>\n'

        return testo
    }// fine del metodo

    //--selettore per il nome del milite
    //--1) tutti i militi in ordine alfabetico
    //--2) solo i militi della funzione
    //--3) prima i militi della funzione e poi gli altri in ordine alfabetico
    private String formFunzioneEditAll(Turno turno, int riga) {
        String testo = ''
        Croce croce = turno.croce
        boolean isMostraSoloMilitiFunzione = croceService.isMostraSoloMilitiFunzione(croce)
        boolean isMostraMilitiFunzioneAndAltri = croceService.isMostraMilitiFunzioneAndAltri(croce)
        boolean usaNomeCognome = croceService.usaNomeCognome(croce.sigla)
        Funzione funzione = getFunzione(turno, riga)
        ArrayList listaMiliti
        def militeId = ''
        def obj
        Milite milite
        String nomeMiliteDelTurnoPerLaFunzione = getNomeMiliteForFunzione(turno, riga, usaNomeCognome)
        String nomeMiliteRigaCorrente = ''

        if (isMostraSoloMilitiFunzione) {
            listaMiliti = militeService.listaMilitiAbilitati(croce, funzione)
        } else {
            if (isMostraMilitiFunzioneAndAltri) {
                listaMiliti = militeService.listaMilitiAbilitatiAndAltri(croce, funzione)
            } else {
                listaMiliti = Milite.findAllByCroce(croce, [sort: 'cognome', order: 'asc'])
            }// fine del blocco if-else
        }// fine del blocco if-else

        if (listaMiliti) {
            listaMiliti.add(0, '')
        }// fine del blocco if

        if (listaMiliti) {
            for (int k = 0; k < listaMiliti.size(); k++) {
                obj = listaMiliti.get(k)
                if (obj instanceof Milite) {
                    milite = (Milite) obj
                    militeId = milite.id
                    if (usaNomeCognome) {
                        nomeMiliteRigaCorrente = milite.getNomeCognome()
                    } else {
                        nomeMiliteRigaCorrente = milite.getCognomeNome()
                    }// fine del blocco if-else
                } else {
                    if (obj instanceof String) {
                        militeId = ''
                        nomeMiliteRigaCorrente = obj
                    }// fine del blocco if
                }// fine del blocco if-else

                if (nomeMiliteRigaCorrente.equals(nomeMiliteDelTurnoPerLaFunzione)) {
                    testo += "<option value=\"${militeId}\" selected=\"selected\">${nomeMiliteRigaCorrente}</option>\n"
                } else {
                    testo += "<option value=\"${militeId}\">${nomeMiliteRigaCorrente}</option>\n"
                }// fine del blocco if-else
            } // fine del ciclo for
        }// fine del blocco if


        return testo
    }// fine del metodo

    //--selettore per il nome del milite
    //--3) solo quelli abilitati alla funzione
    private String formFunzioneEditAbilitati(Turno turno, int riga) {
    }// fine del metodo

    //--selettore per il nome del milite
    //--2) solo il milite loggato
    //--controlla il flag per editare o meno il campo delle funzioni per cui è abilitato
    //--controlla se esiste già un milite selezionato per la funzione
    //--se non è abilitato: valore vuoto o nome del milite già selezionato (da altri) e campo bloccato
    //--se è abilitato ed esiste già il nome di un milite, solo quel nome e campo bloccato
    //--se è abilitato e non c'è milite, riga vuota più nome loggato
    private String formFunzioneEditLoggato(Turno turno, int riga, Funzione funzione) {
        String testo
        boolean isAbilitatoFunzione = militeService.isAbilitatoFunzione(funzione)

        if (isAbilitatoFunzione) {
            testo = formFunzioneEditLoggatoAbilitato(turno, riga, funzione)
        } else {
            testo = formFunzioneEditLoggatoNonAbilitato(turno, riga, funzione)
        }// fine del blocco if-else

        return testo
    }// fine del metodo

    //--selettore per il nome del milite
    //--controlla se esiste già un milite selezionato per la funzione
    //--se è abilitato ed esiste già il nome di un milite, solo quel nome e campo bloccato
    //--se è abilitato e non c'è milite, riga vuota più nome loggato
    private String formFunzioneEditLoggatoAbilitato(Turno turno, int riga, Funzione funzione) {
        String testo = ''
        def militeId = ''
        Milite militeDelTurnoPerLaFunzione = getMiliteForFunzione(turno, riga)
        String nomeMiliteDelTurnoPerLaFunzione
        Milite militeLoggato = militeService.militeLoggato
        String nomeMilite = ''
        boolean isLoggatoGiaSegnato = (militeDelTurnoPerLaFunzione == militeLoggato)

        if (militeDelTurnoPerLaFunzione) {
            militeId = militeDelTurnoPerLaFunzione.id
            nomeMilite = militeDelTurnoPerLaFunzione.toString()
            if (isLoggatoGiaSegnato) {
                testo += "<option value=\"\"></option>\n"
                testo += "<option value=\"${militeId}\" selected=\"selected\">${nomeMilite}</option>\n"
            } else {
                testo += "<option value=\"${militeId}\">${nomeMilite}</option>\n"
            }// fine del blocco if-else
        } else {
            militeId = militeLoggato.id
            nomeMilite = militeLoggato.toString()
            testo += "<option value=\"\" selected=\"selected\"></option>\n"
            testo += "<option value=\"${militeId}\">${nomeMilite}</option>\n"
        }// fine del blocco if-else

        return testo
    }// fine del metodo

    //--selettore per il nome del milite
    //--se non è abilitato: valore vuoto o nome del milite già selezionato (da altri) e campo bloccato
    private static String formFunzioneEditLoggatoNonAbilitato(Turno turno, int riga, Funzione funzione) {
        String testo = ''
        def militeId = ''
        Milite militeDelTurnoPerLaFunzione = getMiliteForFunzione(turno, riga)
        String nomeMiliteDelTurnoPerLaFunzione

        if (militeDelTurnoPerLaFunzione) {
            militeId = militeDelTurnoPerLaFunzione.id
            nomeMiliteDelTurnoPerLaFunzione = militeDelTurnoPerLaFunzione.toString()
        } else {
            militeId = ''
            //nomeMiliteDelTurnoPerLaFunzione = Cost.SPAZIO_30 @todo eventualmente
            nomeMiliteDelTurnoPerLaFunzione = 'funzione non abilitata'
        }// fine del blocco if-else
        testo += "<option value=\"${militeId}\" selected=\"selected\">${nomeMiliteDelTurnoPerLaFunzione}</option>\n"

        return testo
    }// fine del metodo

    private static String getNomeMiliteForFunzione(Turno turno, int numFunzione, boolean usaNomeCognome) {
        String nomeMilite = ''
        Milite milite
        String num = 'militeFunzione' + numFunzione

        milite = turno."${num}"

        if (milite) {
            if (usaNomeCognome) {
                nomeMilite = milite.getNomeCognome()
            } else {
                nomeMilite = milite.getCognomeNome()
            }// fine del blocco if-else
        }// fine del blocco if

        return nomeMilite
    }// fine del metodo

    public static Milite getMiliteForFunzione(Turno turno, int numFunzione) {
        Milite milite = null
        String num = 'militeFunzione' + numFunzione

        if (turno."${num}") {
            milite = turno."${num}"
        }// fine del blocco if

        return milite
    }// fine del metodo

    private static Funzione getFunzione(Turno turno, int numFunzione) {
        Funzione funzione = null
        String num = 'funzione' + numFunzione

        if (turno."${num}") {
            funzione = turno."${num}"
        }// fine del blocco if

        return funzione
    }// fine del metodo

    private static String formBox(Turno turno, int numFunzione) {
        String testo = ''
        String tagNota = ' Nota '
        String label = 'problemiFunzione' + numFunzione
        String value = turno."${label}"
        String tag = ' checked="checked"'

        testo += "<input type=\"hidden\" name=\"_${label}\"/><input type=\"checkbox\""
        if (value == 'true') {
            testo += tag
        }// fine del blocco if
        testo += " name=\"${label}\" id=\"${label}\"/>"
        testo += tagNota

        return testo
    }

    private static String formRadio(Turno turno, String campo, String label) {
        String testo = ''
        String tagSpazio = '  '
        String value = turno."${campo}"
        String tag = ' checked="checked"'

        testo += "<input type=\"hidden\" name=\"_${campo}\"/><input type=\"checkbox\""
        if (value == 'true') {
            testo += tag
        }// fine del blocco if
        testo += " name=\"${campo}\" id=\"${campo}\"/>"
        testo += tagSpazio + label

        return testo
    }

    private static String formRadio(String nome, String label, boolean spuntato) {
        String testoOut = ''
        String tagSpazio = '  '
        String tag = ' checked="checked"'

//        testoOut += "<input type=\"hidden\" name=\"${nome}\"/><input type=\"checkbox\""
        testoOut += "<input type=\"checkbox\""
        if (spuntato) {
            testoOut += tag
        }// fine del blocco if
        testoOut += " name=\"${nome}\" id=\"${nome}\"/>"
        testoOut += tagSpazio + label

        return testoOut
    }// fine del metodo

    private static String formOreMilite(Turno turno, boolean mostraOreSingoloMilite, int numFunzione) {
        String testo = ''
        String label = 'oreMilite' + numFunzione
        String value = turno."${label}"

        if (value.equals('0') && mostraOreSingoloMilite) {
            value = turno.tipoTurno.durata
        }// fine del blocco if

        testo += "<input type=\"text\" name=\"${label}\" value=\"${value}\" style=\"width: 30px\" id=\"${label}\"/>"

        return testo
    }// fine del metodo


    private static String formNote(String testoNota) {
        String testo = ''

        testo += '<textarea class="formnote" name="note" cols="60" rows="2" id="note" >'
        testo += testoNota
        testo += '</textarea>'

        return testo
    }// fine del metodo

    private static String formCaption(Turno turno) {
        String testoOut
        String tag = 'Turno '
        TipoTurno tipoTurno
        String caption = ''

        if (turno && turno.tipoTurno) {
            tipoTurno = turno.tipoTurno
            if (tipoTurno && tipoTurno.sigla) {
                caption = tag + tipoTurno.sigla
            }// fine del blocco if
        }// fine del blocco if

        testoOut = Lib.tagCaption(caption, Aspetto.formcaption)
        return testoOut
    }// fine della closure

    private static String getMeseCorrente(Date giorno) {
        / * variabili e costanti locali di lavoro * /
        String mese = ''
        int pos
        int posGiorno
        int anno
        GregorianCalendar cal = new GregorianCalendar()

        if (giorno) {
            cal.setTime(giorno)
            pos = cal.get(Calendar.MONTH) + 1
            posGiorno = cal.get(Calendar.DAY_OF_MONTH)
            anno = cal.get(Calendar.YEAR)

            if (posGiorno < 24) {
                mese = Mese.getLong(pos)
            } else {
                mese = Mese.getShort(pos)
                mese += ' / '
                if (pos == 12) {
                    pos = 0
                    anno++
                }// fine del blocco if

                mese += Mese.getShort(pos + 1)
            }// fine del blocco if-else

            mese += ' ' + anno
        }// fine del blocco if

        /* valore di ritorno     */
        return mese
    }

    /**
     * Titoli di testa della tabella <br>
     *
     * @giorno nella testata della tabella
     * @return testo del tag
     */
    private static String titoloGiorno(Date giorno) {
        String nome
        String numero
        GregorianCalendar cal = new GregorianCalendar()

        cal.setTime(giorno)
        nome = Lib.getGiorno(giorno)
        numero = cal.get(Calendar.DAY_OF_MONTH)

        return Lib.tagCellaTitolo(nome + ' ' + numero, Aspetto.titoligiorni)
    }// fine del metodo

    /**
     * Crea una data di lunedi.
     * <p/>
     *
     * @param giorno il giorno del mese (1 per il primo)
     * @param mese il mese dell'anno (1 per gennaio)
     * @param anno l'anno
     *
     * @return la data creata
     */
    static Timestamp creaDataLunedi() {
        /* variabili e costanti locali di lavoro */
        Date giorno = new Date()
        Calendar cal

        try { // prova ad eseguire il codice
            cal = Calendar.getInstance()
            cal.setTime(giorno)

            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            giorno = new java.util.Date(cal.getTime().getTime());

            while (cal.get(Calendar.DAY_OF_WEEK) != 2) {
                giorno = giorno - 1
                cal.setTime(giorno)
            }// fine del blocco while

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return giorno.toTimestamp()
    }// fine del metodo

    /**
     * Crea una data di oggi, alla mezzanotte.
     * Ore, minuti e secondi a zero
     *
     * @return la data creata
     */
    static Timestamp creaDataOggi() {
        /* variabili e costanti locali di lavoro */
        Date giorno = new Date()
        Calendar cal

        try { // prova ad eseguire il codice
            cal = Calendar.getInstance()
            cal.setTime(giorno)

            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)

            giorno = new java.util.Date(cal.getTime().getTime());

        } catch (Exception unErrore) { // intercetta l'errore
        }// fine del blocco try-catch

        /* valore di ritorno */
        return giorno.toTimestamp()
    }// fine del metodo

    private static boolean esisteUnTurnoNellaUltimaRiga(TipoTurno tipoTurno, Date inizio, Date fine, int riga) {
        boolean esiste = false
        LinkedHashMap<Date, Turno> mappaTurniDiUnTipo = creaMappaTurniDiUnTipo(tipoTurno, inizio, fine, riga)

        mappaTurniDiUnTipo?.each {
            if (it.value) {
                esiste = true
            }// fine del blocco if
        }

        return esiste
    }// fine del metodo

} // fine della classe TagLib
