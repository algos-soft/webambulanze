/* Created by Algos s.r.l. */
/* Date: mag 2012 */
/* Questo file è stato installato dal plugin AlgosBase */
/* Tipicamente NON verrà più sovrascritto dalle successive release del plugin */
/* in quanto POTREBBE essere personalizzato in questa applicazione */
/* Se vuoi che le prossime release del plugin sovrascrivano questo file, */
/* perdendo tutte le modifiche effettuate qui, */
/* regola a true il flag di controllo flagOverwrite© */
/* flagOverwrite = false */

package webambulanze

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.dao.DataIntegrityViolationException

@Secured([Cost.ROLE_MILITE])
class ViaggioController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def croceService

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def automezzoService

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def turnoService

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def tipoTurnoService

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def viaggioService

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def militeService

    def index() {
        redirect(action: 'list', params: params)
    } // fine del metodo

    def list(Integer max) {
        ArrayList<Viaggio> lista
        Croce croce = croceService.getCroce(request)
        ArrayList campiLista

        //--selezione delle colonne (campi) visibili nella lista
        //--solo nome e di default il titolo viene uguale
        //--mappa con [campo:'nomeDelCampo', title:'titoloVisibile', sort:'ordinamento']
        //--se vuoto, mostra i primi n (stabilito nel templates:scaffoldinf:list)
        //--    nell'ordine stabilito nella constraints della DomainClass
        campiLista = [
                [campo: 'numeroServizio', title: '#croce'],
                [campo: 'numeroViaggio', title: '#mezzo'],
                [campo: 'numeroBolla', title: '#bolla'],
                [campo: 'tipoViaggio', title: 'tipo'],
                [campo: 'automezzo', title: 'automezzo'],
//                [campo: 'giorno', title: 'data'],
                [campo: 'turno', title: 'turno'],
                [campo: 'chilometriPercorsi', title: 'km reali'],
                [campo: 'chilometriArrivo', title: 'km tot'],
                [campo: 'chilometriFattura', title: 'km fatt'],
                [campo: 'durata', title: 'min'],
                [campo: 'codiceInvio', title: 'cod invio']
        ]

        if (params.order) {
            if (params.order == 'asc') {
                params.order = 'desc'
            } else {
                params.order = 'asc'
            }// fine del blocco if-else
        } else {
            params.order = 'asc'
        }// fine del blocco if-else

        if (croce) {
            params.siglaCroce = croce.sigla
            if (params.siglaCroce.equals(Cost.CROCE_ALGOS)) {
                lista = Viaggio.findAll("from Viaggio order by croce_id,giorno")
                campiLista = ['id', 'croce'] + campiLista
            } else {
                params.sort = 'giorno'
                params.order = 'desc'
                java.lang.Object obj = params;

                lista = Viaggio.findAllByCroce(croce, params)
            }// fine del blocco if-else
        } else {
            lista = Viaggio.findAll(params)
        }// fine del blocco if-else

//        sistemaDurata(lista)
//        media(lista)

//        flash.errors = ''
        render(view: 'list', model: [titoloLista: 'prot', viaggioInstanceList: lista, viaggioInstanceTotal: 0, campiLista: campiLista], params: params)
    } // fine del metodo

    private static void sistemaDurata(ArrayList<Viaggio> lista) {
        int totale = 0
        int numViaggi = 0
        def media

        lista?.each {
            if (!it.durata) {
                it.beforeRegolaDurata()
                it.save()
            }// fine del blocco if
        } // fine del ciclo each

    } // fine del metodo

    private static void media(ArrayList<Viaggio> lista) {
        int totale = 0
        int numViaggi = 0
        def media

        lista?.each {
            if (ViaggioService.isDurataCorretta(it)) {
                totale += ViaggioService.durata(it)
                numViaggi++
                it.save()
            } else {
                def stop
            }// fine del blocco if-else
        } // fine del ciclo each

        media = totale / numViaggi
        media = media.intValue()

        def stop
    } // fine del metodo

    def listaMezzo(Long id) {
        def lista
        String titolo
        String nomeMezzo = ''
        Croce croce = croceService.getCroce(request)
        Automezzo mezzo = null
        ArrayList campiLista = null

        //--selezione delle colonne (campi) visibili nella lista
        //--solo nome e di default il titolo viene uguale
        //--mappa con [campo:'nomeDelCampo', title:'titoloVisibile', sort:'ordinamento']
        //--se vuoto, mostra i primi n (stabilito nel templates:scaffoldinf:list)
        //--    nell'ordine stabilito nella constraints della DomainClass
        campiLista = [
                [campo: 'numeroServizio', title: '#servizio'],
                [campo: 'numeroViaggio', title: '#mezzo'],
                [campo: 'numeroBolla', title: '#bolla'],
                [campo: 'tipoViaggio', title: 'tipo'],
                [campo: 'automezzo', title: 'automezzo'],
                [campo: 'giorno', title: 'data'],
                [campo: 'turno', title: 'turno'],
                [campo: 'chilometriPercorsi', title: 'km reali'],
                [campo: 'chilometriArrivo', title: 'km fattura'],
                [campo: 'codiceInvio', title: 'cod invio']
        ]

        if (params.order) {
            if (params.order == 'asc') {
                params.order = 'desc'
            } else {
                params.order = 'asc'
            }// fine del blocco if-else
        } else {
            params.order = 'asc'
        }// fine del blocco if-else

        if (id) {
            mezzo = Automezzo.findById(id)
            nomeMezzo = mezzo.targa
        }// fine del blocco if

        if (croce) {
            params.siglaCroce = croce.sigla
            if (params.siglaCroce.equals(Cost.CROCE_ALGOS)) {
                lista = Viaggio.findAll("from Viaggio order by croce_id, giorno")
                campiLista = ['id', 'croce'] + campiLista
            } else {
                lista = Viaggio.findAllByAutomezzo(mezzo)
            }// fine del blocco if-else
        } else {
            lista = Viaggio.findAll(params)
        }// fine del blocco if-else

        flash.errors = ''
        titolo = "Viaggi del mezzo ${nomeMezzo}"
        render(view: 'list', model: [titolo: titolo, viaggioInstanceList: lista, viaggioInstanceTotal: 0, campiLista: campiLista], params: params)
    } // fine del metodo

    def create() {
        Croce croce = croceService.getCroce(request)
        params.siglaCroce = croceService.getSiglaCroce(request)
        ArrayList listaTipiViaggioSigla
        ArrayList listaTipiViaggioDescrizione
        ArrayList listaAutomezzi
        ArrayList listaTurniId
        ArrayList listaTurniTxt
        ArrayList listaGiorniNum
        ArrayList listaGiorniTxt
        String tipoSelezionato
        int giornoSelezionato
        String turnoSelezionato

        //--selezione
        listaTipiViaggioSigla = TipoViaggio.getListaSigla()
        listaTipiViaggioDescrizione = TipoViaggio.getListaNome()
        tipoSelezionato = listaTipiViaggioSigla[0]

        //--automezzo
        listaAutomezzi = automezzoService.getAllTarga(croce)

        //--giorno
        listaGiorniNum = viaggioService.listaGiorniNum()
        listaGiorniTxt = viaggioService.listaGiorniTxt()
        giornoSelezionato = 0

        //-- turno
        listaTurniId = tipoTurnoService.getListaTurniId(croce)
        listaTurniTxt = tipoTurnoService.getListaTurni(croce)
        turnoSelezionato = listaTurniTxt.get(3)


        render(view: 'selezione', model: [
                listaTipiViaggioSigla      : listaTipiViaggioSigla,
                listaTipiViaggioDescrizione: listaTipiViaggioDescrizione,
                tipoSelezionato            : tipoSelezionato,
                listaAutomezzi             : listaAutomezzi,
                listaGiorniNum             : listaGiorniNum,
                listaGiorniTxt             : listaGiorniTxt,
                giornoSelezionato          : giornoSelezionato,
                listaTurniId               : listaTurniId,
                listaTurniTxt              : listaTurniTxt,
                turnoSelezionato           : turnoSelezionato],
                params: params)
    } // fine del metodo

    def nuovo118() {
        params.tipoViaggio = '118'
        redirect(action: 'selezionemezzo', params: params)
    } // fine del metodo

    def nuovoOrdinario() {
        params.tipoViaggio = 'ordinario'
        render(view: 'selezionemancante', params: params)
//        redirect(action: 'selezionemezzo', params: params)
    } // fine del metodo

    def nuovoDializzati() {
        params.tipoViaggio = 'dializzati'
        render(view: 'selezionemancante', params: params)
//        redirect(action: 'selezionemezzo', params: params)
    } // fine del metodo

    def nuovoInterno() {
        params.tipoViaggio = 'interno'
        render(view: 'selezionemancante', params: params)
//        redirect(action: 'selezionemezzo', params: params)
    } // fine del metodo

    def selezionemezzo() {
        params.siglaCroce = croceService.getSiglaCroce(request)
        def tipoViaggio = params.tipoViaggio
        render(view: 'selezionemezzo', model: [viaggioInstance: new Viaggio(params), tipoViaggio: tipoViaggio], params: params)
    } // fine del metodo

    def nuovoViaggio() {
        Croce croce = croceService.getCroce(request)
        params.siglaCroce = croceService.getSiglaCroce(request)
        Automezzo automezzo
        Turno turno
        TipoTurno tipoTurno
        long turnoId = 0
        long tipoTurnoId
        long automezzoId
        Settings settingsCroce
        Date oggi = Lib.creaDataOggi()
        Date giorno = null
        String siglaAutomezzo
        String tipoForm = 'Crea viaggio 118'
        int numGiorniIndietroDaOggi
        String tipoViaggioSigla = ''
        def a = params

        //--controlla che sia selezionato il tipo di viaggio
        if (params.tipoViaggio && params.tipoViaggio[1]) {
            tipoViaggioSigla = params.tipoViaggio[1]
            if (tipoViaggioSigla.equals('null')) {
                flash.errors = "Devi selezionare una tipologia di servizio, prima di proseguire"
                redirect(action: 'create')
                return
            }// fine del blocco if
        }// fine del blocco if

        if (params.auto) {
            automezzo = Automezzo.findByCroceAndTarga(croce, params.auto)
            if (automezzo) {
                params.automezzo = automezzo
                params.numeroViaggio = automezzo.numeroViaggiEffettuati + 1
                params.chilometriPartenza = automezzo.chilometriTotaliPercorsi
                params.chilometriArrivo = automezzo.chilometriTotaliPercorsi
            } else {
                flash.errors = "Devi selezionare un automezzo, prima di proseguire"
                redirect(action: 'create')
                return
            }// fine del blocco if-else
        }// fine del blocco if

        if (params.giorno) {
            try { // prova ad eseguire il codice
                numGiorniIndietroDaOggi = Integer.decode(params.giorno)
                giorno = oggi - numGiorniIndietroDaOggi
            } catch (Exception unErrore) { // intercetta l'errore
                log.error unErrore
            }// fine del blocco try-catch
        } else {
            flash.errors = "Devi selezionare un giorno ed un turno, prima di proseguire. Per giorni più vecchi di una settimana, occorre loggarsi come admin."
            redirect(action: 'create')
        }// fine del blocco if-else

        if (params.tipoTurno && !params.tipoTurno.equals('null')) {
            try { // prova ad eseguire il codice
                tipoTurnoId = Long.decode(params.tipoTurno)
                if (tipoTurnoId) {
                    tipoTurno = TipoTurno.findById(tipoTurnoId)
                    if (tipoTurno) {
                        turno = Turno.findByCroceAndGiornoAndTipoTurno(croce, giorno, tipoTurno)
                        if (turno) {
                            turnoId = turno.id
                            if (turno.militeFunzione1) {
                                params.autistaEmergenza = turno.militeFunzione1
                            }// fine del blocco if
                            if (turno.militeFunzione2) {
                                params.soccorritoreDae = turno.militeFunzione2
                            }// fine del blocco if
                            if (turno.militeFunzione3) {
                                params.soccorritore = turno.militeFunzione3
                            }// fine del blocco if
                            if (turno.militeFunzione4) {
                                params.barelliereAffiancamento = turno.militeFunzione4
                            }// fine del blocco if
                        }// fine del blocco if
                    }// fine del blocco if
                }// fine del blocco if
            } catch (Exception unErrore) { // intercetta l'errore
                log.error unErrore
            }// fine del blocco try-catch
        } else {
            flash.errors = "Devi selezionare un giorno ed un turno, prima di proseguire. Per giorni più vecchi di una settimana, occorre loggarsi come admin."
            redirect(action: 'create')
        }// fine del blocco if-else

        if (croce) {
            settingsCroce = croce.settings
            if (settingsCroce) {
                params.numeroServizio = settingsCroce.numeroServiziEffettuati + 1
            }// fine del blocco if
        }// fine del blocco if

        //--valori suggeriti
        if (true) {
//            params.tipoViaggio = TipoViaggio.auto118
            params.codiceInvio = CodiceInvio.verde
            params.luogoEvento = LuogoEvento.Z
            params.codiceRicovero = CodiceRicovero.normale
            params.patologia = Patologia.C20
        }// fine del blocco if

        if (true) {
            params.giorno = giorno
            //params.inizio = inizio
            //params.fine = fine
        }// fine del blocco if

        if (true) {
            render(view: 'create', model: [
                    tipoForm        : tipoForm,
                    tipoViaggioSigla: tipoViaggioSigla,
                    automezzoId     : params.automezzo.id,
                    turnoId         : turnoId],
                    params: params)
        } else {
            render(view: 'selezionemancante', params: params)
        }// fine del blocco if-else
    } // fine del metodo

    def save() {
        Milite milite = null
        Turno turno = null
        int inizioOre = 0
        int inizioMinuti = 0
        int rientroOre = 0
        int rientroMinuti = 0
        Date giorno = null

        if (params.list) {
            redirect(action: 'list')
            return
        }// fine del blocco if
        def pippoz = params

        if (params.turnoId) {
            turno = Turno.findById(params.turnoId)
            giorno = turno.giorno
            params.giorno = giorno
        }// fine del blocco if

        if (params.tipoViaggioSigla) {
            params.tipoViaggio = TipoViaggio.getDaSigla(params.tipoViaggioSigla)
        }// fine del blocco if

        if (params.automezzoId) {
            params.automezzo = Automezzo.findById(params.automezzoId)
        }// fine del blocco if

        //@todo da controllare
        if (!params.inizio) {
            params.inizio = params.giorno
        }// fine del blocco if

        //@todo da controllare
        if (!params.fine) {
            params.fine = params.giorno
        }// fine del blocco if

        if (params.codiceInvio) {
            params.codiceInvio = CodiceInvio.getDaNome(params.codiceInvio)
        }// fine del blocco if

        if (params.luogoEvento) {
            params.luogoEvento = LuogoEvento.getDaNome(params.luogoEvento)
        }// fine del blocco if

        if (params.patologia) {
            params.patologia = Patologia.getDaNome(params.patologia)
        }// fine del blocco if

        if (params.codiceRicovero) {
            params.codiceRicovero = CodiceRicovero.getDaNome(params.codiceRicovero)
        }// fine del blocco if

        if (!params.numeroCartellino) {
            params.numeroCartellino = 'ord'
        }// fine del blocco if

        if (params.chilometriPartenza) {
            params.chilometriPartenza = Integer.decode(params.chilometriPartenza)
        }// fine del blocco if

        if (params.chilometriArrivo) {
            params.chilometriArrivo = Integer.decode(params.chilometriArrivo)
        }// fine del blocco if

        if (params.chilometriPartenza && params.chilometriArrivo) {
            params.chilometriPercorsi = params.chilometriArrivo - params.chilometriPartenza
        }// fine del blocco if

        if (params.militeFunzione1) {
            params.militeFunzione1 = Milite.findById(params.militeFunzione1)
        } else {
            if (turno) {
                params.militeFunzione1 = AmbulanzaTagLib.getMiliteForFunzione(turno, 1)
            }// fine del blocco if
        }// fine del blocco if-else

        if (params.militeFunzione2) {
            params.militeFunzione2 = Milite.findById(params.militeFunzione2)
        } else {
            if (turno) {
                params.militeFunzione2 = AmbulanzaTagLib.getMiliteForFunzione(turno, 2)
            }// fine del blocco if
        }// fine del blocco if-else

        if (params.militeFunzione3) {
            params.militeFunzione3 = Milite.findById(params.militeFunzione3)
        } else {
            if (turno) {
                params.militeFunzione3 = AmbulanzaTagLib.getMiliteForFunzione(turno, 3)
            }// fine del blocco if
        }// fine del blocco if-else

        if (params.militeFunzione4) {
            params.militeFunzione4 = Milite.findById(params.militeFunzione4)
        } else {
            if (turno) {
                params.militeFunzione4 = AmbulanzaTagLib.getMiliteForFunzione(turno, 4)
            }// fine del blocco if
        }// fine del blocco if-else

        if (params.inizioOre) {
            inizioOre = Integer.decode(params.inizioOre)
        }// fine del blocco if

        if (params.inizioMinuti) {
            inizioMinuti = Integer.decode(params.inizioMinuti)
        }// fine del blocco if

        if (params.rientroOre) {
            rientroOre = Integer.decode(params.rientroOre)
        }// fine del blocco if

        if (params.rientroMinuti) {
            rientroMinuti = Integer.decode(params.rientroMinuti)
        }// fine del blocco if

        params.inizio = Lib.setOra(giorno, inizioOre)
        params.inizio = Lib.setMinuto(params.inizio, inizioMinuti)
        params.fine = Lib.setOra(giorno, rientroOre)
        params.fine = Lib.setMinuto(params.fine, rientroMinuti)

        def viaggioInstance = new Viaggio(params)
        Croce croce = croceService.getCroce(request)

        if (croce) {
            params.siglaCroce = croce.sigla
            if (!viaggioInstance.croce) {
                viaggioInstance.croce = croce
            }// fine del blocco if
        }// fine del blocco if

        if (turno) {
            viaggioInstance.turno = turno
        }// fine del blocco if

        if (params.automezzo) {
            viaggioInstance.chilometriPartenza = params.automezzo.chilometriTotaliPercorsi
        }// fine del blocco if

        if (params.numeroServizio) {
            croceService.setNumeroServiziEffettuati(croce, Integer.decode(params.numeroServizio))
        }// fine del blocco if

        //--controllo chilometraggio
        if (true) {
            if (viaggioInstance.chilometriArrivo == 0) {
                flash.errors = "Viaggio non registrato - Non hai segnato i chilometri all'arrivo"
                redirect(action: 'list')
                return
            }// fine del blocco if
            if (viaggioInstance.chilometriArrivo == viaggioInstance.chilometriPartenza) {
                flash.errors = "Viaggio non registrato - Ti sei dimenticato di modificare i chilometri all'arrivo"
                redirect(action: 'list')
                return
            }// fine del blocco if
            if (viaggioInstance.chilometriArrivo < viaggioInstance.chilometriPartenza) {
                flash.errors = "Viaggio non registrato - I chilometri all'arrivo non possono essere minori di quelli alla partenza"
                redirect(action: 'list')
                return
            }// fine del blocco if
        }// fine del blocco if

        if (!viaggioInstance.save(flush: true)) {
            flash.errors = "Viaggio non registrato - Si è verificato un errore"
            redirect(action: 'list')
            return
        }// fine del blocco if

        if (viaggioInstance) {
            afterRegolaAutomezzo(viaggioInstance.id)
            afterRegolaSettingsCroce(viaggioInstance.id)
        }// fine del blocco if

//        flash.message = message(code: 'default.created.message', args: [message(code: 'viaggio.label', default: 'Viaggio'), viaggioInstance.id])
        flash.message = 'Registrato il servizio n° ' + viaggioInstance.numeroServizio
        redirect(action: 'list')
    } // fine del metodo

    def show(Long id) {
        def viaggioInstance = Viaggio.get(id)
        if (!viaggioInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'viaggio.label', default: 'Viaggio'), id])
            redirect(action: 'list')
            return
        }

        [viaggioInstance: viaggioInstance]
    } // fine del metodo

    def edit(Long id) {
        def viaggioInstance = Viaggio.get(id)
        if (!viaggioInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'viaggio.label', default: 'Viaggio'), id])
            redirect(action: 'list')
            return
        }

        [viaggioInstance: viaggioInstance]
    } // fine del metodo

    def update(Long id, Long version) {
        int inizioOre = 0
        int inizioMinuti = 0
        int rientroOre = 0
        int rientroMinuti = 0

        def viaggioInstance = Viaggio.get(id)

        if (!viaggioInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'viaggio.label', default: 'Viaggio'), id])
            redirect(action: 'list')
            return
        }

        if (version != null) {
            if (viaggioInstance.version > version) {
                viaggioInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'viaggio.label', default: 'Viaggio')] as Object[],
                        "Another user has updated this Viaggio while you were editing")
                render(view: "edit", model: [viaggioInstance: viaggioInstance])
                return
            }
        }

        if (params.codiceInvio) {
            params.codiceInvio = CodiceInvio.getDaNome(params.codiceInvio)
        }// fine del blocco if

        if (params.luogoEvento) {
            params.luogoEvento = LuogoEvento.getDaNome(params.luogoEvento)
        }// fine del blocco if

        if (params.patologia) {
            params.patologia = Patologia.getDaNome(params.patologia)
        }// fine del blocco if

        if (params.codiceRicovero) {
            params.codiceRicovero = CodiceRicovero.getDaNome(params.codiceRicovero)
        }// fine del blocco if

        if (params.inizioOre) {
            inizioOre = Integer.decode(params.inizioOre)
        }// fine del blocco if

        if (params.inizioMinuti) {
            inizioMinuti = Integer.decode(params.inizioMinuti)
        }// fine del blocco if

        if (params.rientroOre) {
            rientroOre = Integer.decode(params.rientroOre)
        }// fine del blocco if

        if (params.rientroMinuti) {
            rientroMinuti = Integer.decode(params.rientroMinuti)
        }// fine del blocco if

        params.giorno = Lib.setOra(params.giorno, 0)
        params.giorno = Lib.setMinuto(params.giorno, 0)
        params.giorno = Lib.setSecondo(params.giorno, 0)
        params.inizio = Lib.setOra(params.giorno, inizioOre)
        params.inizio = Lib.setMinuto(params.inizio, inizioMinuti)
        params.inizio = Lib.setSecondo(params.inizio, 0)
        params.fine = Lib.setOra(params.giorno, rientroOre)
        params.fine = Lib.setMinuto(params.fine, rientroMinuti)
        params.fine = Lib.setSecondo(params.fine, 0)

        viaggioInstance.properties = params

        if (!viaggioInstance.save(flush: true)) {
            render(view: "edit", model: [viaggioInstance: viaggioInstance])
            return
        }

        if (viaggioInstance) {
            afterRegolaAutomezzo(viaggioInstance.id)
            afterRegolaSettingsCroce(viaggioInstance.id)
        }// fine del blocco if

        flash.message = message(code: 'default.updated.message', args: [message(code: 'viaggio.label', default: 'Viaggio'), viaggioInstance.id])
        redirect(action: 'list')
    } // fine del metodo

    /**
     * metodo chiamato dopo aver creato o modificato un record
     */
    public afterRegolaAutomezzo(Long id) {
        Automezzo auto
        def viaggioInstance = Viaggio.get(id)

        if (viaggioInstance) {
            auto = viaggioInstance.automezzo
            if (auto) {
                auto.chilometriTotaliPercorsi = viaggioInstance.chilometriArrivo
                auto.numeroViaggiEffettuati = viaggioInstance.numeroViaggio
                auto.save(flush: true)
            }// fine del blocco if
        }// fine del blocco if
    } // fine del metodo

    /**
     * metodo chiamato dopo aver creato o modificato un record
     */
    public afterRegolaSettingsCroce(Long id) {
        Croce croce = croceService.getCroce(request)
        Settings settingsCroce
        def viaggioInstance = Viaggio.get(id)

        if (croce) {
            settingsCroce = croce.settings
            if (settingsCroce) {
                settingsCroce.numeroServiziEffettuati = viaggioInstance.numeroServizio
            }// fine del blocco if
        }// fine del blocco if
    } // fine del metodo

    @Secured([Cost.ROLE_ADMIN])
    def delete(Long id) {
        def viaggioInstance = Viaggio.get(id)
        if (!viaggioInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'viaggio.label', default: 'Viaggio'), id])
            redirect(action: "list")
            return
        }

        try {
            viaggioInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'viaggio.label', default: 'Viaggio'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'viaggio.label', default: 'Viaggio'), id])
            redirect(action: "show", id: id)
        }
    } // fine del metodo


    public static int deleteLink(Milite milite) {
        int status = 0 //indeterminato
        def listaViaggiMiliteFunzione1 = Viaggio.findAllByMiliteFunzione1(milite)
        def listaViaggiMiliteFunzione2 = Viaggio.findAllByMiliteFunzione2(milite)
        def listaViaggiMiliteFunzione3 = Viaggio.findAllByMiliteFunzione3(milite)
        def listaViaggiMiliteFunzione4 = Viaggio.findAllByMiliteFunzione4(milite)

        if (listaViaggiMiliteFunzione1.isEmpty()
                && listaViaggiMiliteFunzione2.isEmpty()
                && listaViaggiMiliteFunzione3.isEmpty()
                && listaViaggiMiliteFunzione4.isEmpty()) {
            status = 1 //non esiste
        } else {
            status = 2 //non riesco a cancellarlo
            if (listaViaggiMiliteFunzione1 != null) {
                for (Viaggio viaggio : listaViaggiMiliteFunzione1) {
                    viaggio.militeFunzione1 = null;
                    try { // prova ad eseguire il codice
                        viaggio.save(flush: true)
                        status = 3 //eliminato riferimento
                    } catch (Exception unErrore) { // intercetta l'errore
                    }// fine del blocco try-catch
                }// end of for cycle
            }// end of if cycle
            if (listaViaggiMiliteFunzione2 != null) {
                for (Viaggio viaggio : listaViaggiMiliteFunzione2) {
                    viaggio.militeFunzione2 = null;
                    try { // prova ad eseguire il codice
                        viaggio.save(flush: true)
                        status = 3 //eliminato riferimento
                    } catch (Exception unErrore) { // intercetta l'errore
                    }// fine del blocco try-catch
                }// end of for cycle
            }// end of if cycle
            if (listaViaggiMiliteFunzione3 != null) {
                for (Viaggio viaggio : listaViaggiMiliteFunzione3) {
                    viaggio.militeFunzione3 = null;
                    try { // prova ad eseguire il codice
                        viaggio.save(flush: true)
                        status = 3 //eliminato riferimento
                    } catch (Exception unErrore) { // intercetta l'errore
                    }// fine del blocco try-catch
                }// end of for cycle
            }// end of if cycle
            if (listaViaggiMiliteFunzione4 != null) {
                for (Viaggio viaggio : listaViaggiMiliteFunzione4) {
                    viaggio.militeFunzione4 = null;
                    try { // prova ad eseguire il codice
                        viaggio.save(flush: true)
                        status = 3 //eliminato riferimento
                    } catch (Exception unErrore) { // intercetta l'errore
                    }// fine del blocco try-catch
                }// end of for cycle
            }// end of if cycle

        }// end of if/else cycle

        return status
    } // fine del metodo


} // fine della controller classe
