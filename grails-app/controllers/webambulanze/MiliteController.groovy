package webambulanze

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.dao.DataIntegrityViolationException

//@Secured([Cost.ROLE_MILITE])
class MiliteController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    private static boolean EDIT_VERSO_LISTA = true

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def militeService

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def funzioneService

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def logoService

    // utilizzo di un service con la businessLogic per l'elaborazione dei dati
    // il service viene iniettato automaticamente
    def croceService

    def index() {
        redirect(action: 'list', params: params)
    } // fine del metodo

    //-deprecated
    def statistiche() {
        def lista
        Croce croce
        String sigla
        def campiLista = [
                'cognome',
                'nome',
                'turniAnno',
                'oreAnno']
        def campiExtra = null

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
                lista = Milite.findAll("from Milite order by croce_id,cognome")
                campiLista = ['id', 'croce'] + campiLista
            } else {
                if (!params.sort) {
                    params.sort = 'cognome'
                }// fine del blocco if-else
                if (militeService.isLoggatoAdminOrMore()) {
                    lista = Milite.findAllByCroce(croce, params)
                } else {
                    lista = militeService.militeLoggato
                }// fine del blocco if-else
                campiExtra = funzioneService.campiExtraPerCroce(croce)
            }// fine del blocco if-else
        } else {
            lista = Milite.findAll(params)
        }// fine del blocco if-else

        render(view: 'militeturno', model: [
                militeInstanceList : lista,
                militeInstanceTotal: 0,
                campiLista         : campiLista,
                campiExtra         : campiExtra],
                params: params)
    } // fine del metodo


    def list(Integer max) {
        def lista
        Croce croce = croceService.getCroce(request)
        String name = g.cookie(name: 'siglaCroce')

        def campiLista = [
                'cognome',
                'nome',
                'attivo',
                'telefonoCellulare',
                'scadenzaBLSD',
                'scadenzaTrauma',
                'scadenzaNonTrauma']
        def campiExtra = null

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
                lista = Milite.findAll("from Milite order by croce_id,cognome")
                campiLista = ['id', 'croce'] + campiLista
            } else {
                if (!params.sort) {
                    params.sort = 'cognome'
                }// fine del blocco if-else
                lista = Milite.findAllByCroce(croce, params)
//                if (militeService.isLoggatoAdminOrMore()) {
//                    lista = Milite.findAllByCroce(croce, params)
//                } else {
//                    lista = militeService.militeLoggato
//                }// fine del blocco if-else
                campiExtra = funzioneService.campiExtraPerCroce(croce)
            }// fine del blocco if-else
        } else {
            lista = Milite.list(params)
        }// fine del blocco if-else

        render(view: 'list', model: [
                militeInstanceList : lista,
                militeInstanceTotal: 0,
                campiLista         : campiLista,
                campiExtra         : campiExtra],
                params: params)
    } // fine del metodo

    @Secured([Cost.ROLE_ADMIN])
    def create() {
        params.siglaCroce = croceService.getSiglaCroce(request)

        def campiExtra = funzioneService.campiExtra(request)
        render(view: 'create', model: [militeInstance: new Milite(params), campiExtra: campiExtra], params: params)
    } // fine del metodo

    @Secured([Cost.ROLE_ADMIN])
    def save() {
        def militeInstance = new Milite(params)
        Croce croce = croceService.getCroce(request)

        if (croce) {
            params.siglaCroce = croce.sigla
            if (!militeInstance.croce) {
                militeInstance.croce = croce
            }// fine del blocco if
        }// fine del blocco if

        if (!militeInstance.save(flush: true)) {
            render(view: 'create', model: [militeInstance: militeInstance], params: params)
            return
        }// fine del blocco if

        flash.message = logoService.setWarn(Evento.militeCreato, militeInstance)

        //--sicronizza le funzioni del milite nella tavola d'incrocio Militefunzione
        params.croce = militeInstance.croce
        militeService.registraFunzioni(params, militeInstance)
        militeService.regolaFunzioniAutomatiche(militeInstance)

        if (EDIT_VERSO_LISTA) {
            redirect(action: 'list')
        } else {
            redirect(action: 'show', id: militeInstance.id)
        }// fine del blocco if-else
    } // fine del metodo

    def show(Long id) {
        boolean militeSeStesso = false
        def militeInstance = Milite.get(id)
        Milite milite = militeService.militeLoggato

        if (milite) {
            militeSeStesso = militeInstance.id == milite.id
        }// fine del blocco if

        if (militeSeStesso || militeService.isLoggatoAdminOrMore()) {
            redirect(action: 'showEffectively', id: militeInstance.id)
        } else {
            flash.message = 'Sorry, non puoi vedere la pagina di un altro Milite'
            redirect(action: 'list')
        }// fine del blocco if-else

    } // fine del metodo

    def showEffectively(Long id) {
        def militeInstance = Milite.get(id)
        def campiExtra = funzioneService.campiExtra(request)

        if (!militeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'milite.label', default: 'Milite'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if

        params.siglaCroce = croceService.getSiglaCroce(request)
        render(view: 'show', model: [militeInstance: militeInstance, campiExtra: campiExtra], params: params)
    } // fine del metodo


    @Secured([Cost.ROLE_ADMIN])
    def edit(Long id) {
        def militeInstance = Milite.get(id)
        def campiExtra = funzioneService.campiExtra(request)

        if (!militeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'milite.label', default: 'Milite'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if

        params.siglaCroce = croceService.getSiglaCroce(request)
        render(view: 'edit', model: [militeInstance: militeInstance, campiExtra: campiExtra], params: params)
    } // fine del metodo

    @Secured([Cost.ROLE_ADMIN])
    def update(Long id, Long version) {
        def militeInstance = Milite.get(id)

        if (!militeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'milite.label', default: 'Milite'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if

        params.siglaCroce = croceService.getSiglaCroce(request)
        if (version != null) {
            if (militeInstance.version > version) {
                militeInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'milite.label', default: 'Milite')] as Object[],
                        "Another user has updated this Milite while you were editing")
                render(view: 'edit', model: [militeInstance: militeInstance], params: params)
                return
            }// fine del blocco if
        }// fine del blocco if

        flash.listaMessaggi = militeService.avvisoModifiche(params, militeInstance)
        militeInstance.properties = params

        if (!militeInstance.save(flush: true)) {
            render(view: 'edit', model: [militeInstance: militeInstance], params: params)
            return
        }// fine del blocco if

        //--sicronizza le funzioni del milite nella tavola d'incrocio Militefunzione
        params.croce = militeInstance.croce
        militeService.registraFunzioni(params, militeInstance)
        militeService.regolaFunzioniAutomatiche(militeInstance)

        //  flash.message = message(code: 'default.updated.message', args: [message(code: 'milite.label', default: 'Milite'), militeInstance.id])
        if (EDIT_VERSO_LISTA) {
            redirect(action: 'list')
        } else {
            redirect(action: 'show', id: militeInstance.id)
        }// fine del blocco if-else
    } // fine del metodo

    @Secured([Cost.ROLE_PROG])
    def delete(Long id) {
        def militeInstance = Milite.get(id)
        int status
        flash.listaMessaggi = new ArrayList()
        flash.listaErrori = new ArrayList()

        if (!militeInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'milite.label', default: 'Milite'), id])
            redirect(action: 'list')
            return
        }// fine del blocco if

        String milite = militeInstance.toString();
        try {
            status = ViaggioController.deleteLink(militeInstance)
            switch (status) {
                case 0:
                    flash.listaErrori.add('Qualcosa non ha funzionato')
                    break;
                case 1:
                    flash.listaMessaggi.add('Al momento ' + milite + ' non ha effettuato nessun viaggio registrato nei viaggi')
                    break;
                case 2:
                    flash.listaErrori.add('Non riesco a cancellare ' + milite + ' Prova a controllare i viaggi')
                    break;
                case 3:
                    flash.listaMessaggi.add('Eliminati tutti i riferimenti di ' + milite + ' nei viaggi registrati')
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement


            status = LogoController.deleteLink(militeInstance)
            switch (status) {
                case 0:
                    flash.listaErrori.add('Qualcosa non ha funzionato')
                    break;
                case 1:
                    flash.listaMessaggi.add('Al momento ' + milite + ' non ha nessun log')
                    break;
                case 2:
                    flash.listaErrori.add('Non riesco a cancellare ' + milite + ' Prova a controllare i logs')
                    break;
                case 3:
                    flash.listaMessaggi.add('Eliminati tutti i log di ' + milite)
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement

            status = UtenteRuoloController.deleteLink(militeInstance)
            switch (status) {
                case 0:
                    flash.listaErrori.add('Qualcosa non ha funzionato')
                    break;
                case 1:
                    flash.listaMessaggi.add('Al momento ' + milite + ' non ha ruoli attivi')
                    break;
                case 2:
                    flash.listaErrori.add('Non riesco a cancellare ' + milite + ' Prova a controllare la tabella utente/ruolo')
                    break;
                case 3:
                    flash.listaMessaggi.add('Eliminato il ruolo di ' + milite)
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement

            status = TurnoController.deleteLink(militeInstance)
            switch (status) {
                case 0:
                    flash.listaErrori.add('Qualcosa non ha funzionato')
                    break;
                case 1:
                    flash.listaMessaggi.add('Al momento ' + milite + ' non ha partecipato a nessun turno')
                    break;
                case 2:
                    flash.listaErrori.add('Non riesco a cancellare ' + milite + ' Prova a controllare i turni')
                    break;
                case 3:
                    flash.listaMessaggi.add('Eliminata la partecipazione di ' + milite + ' ai turni')
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement


            status = MiliteturnoController.deleteLink(militeInstance)
            switch (status) {
                case 0:
                    flash.listaErrori.add('Qualcosa non ha funzionato')
                    break;
                case 1:
                    flash.listaMessaggi.add('Al momento ' + milite + ' non è presente nelle statistiche militeturno')
                    break;
                case 2:
                    flash.listaErrori.add('Non riesco a cancellare ' + milite + ' Prova a controllare le statistiche militeturno')
                    break;
                case 3:
                    flash.listaMessaggi.add('Eliminato ' + milite + ' dalle statistiche militeturno')
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement

            status = MilitestatisticheController.deleteLink(militeInstance)
            switch (status) {
                case 0:
                    flash.listaErrori.add('Qualcosa non ha funzionato')
                    break;
                case 1:
                    flash.listaMessaggi.add('Al momento ' + milite + ' non è presente nelle statistiche')
                    break;
                case 2:
                    flash.listaErrori.add('Non riesco a cancellare ' + milite + ' Prova a controllare le statistiche')
                    break;
                case 3:
                    flash.listaMessaggi.add('Eliminato ' + milite + ' dalle statistiche')
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement

            status = MilitefunzioneController.deleteLink(militeInstance)
            switch (status) {
                case 0:
                    flash.listaErrori.add('Qualcosa non ha funzionato')
                    break;
                case 1:
                    flash.listaMessaggi.add('Al momento ' + milite + ' non è presente nella tabella delle funzioni di ogni milite')
                    break;
                case 2:
                    flash.listaErrori.add('Non riesco a cancellare ' + milite + ' Prova a controllare la tabella delle funzioni di ogni milite')
                    break;
                case 3:
                    flash.listaMessaggi.add('Eliminate le funzioni abilitate di ' + milite)
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement

            status = UtenteController.deleteLink(militeInstance)
            switch (status) {
                case 0:
                    flash.listaErrori.add('Qualcosa non ha funzionato')
                    break;
                case 1:
                    flash.listaMessaggi.add('Al momento ' + milite + ' non è presente nella lista utenti/password')
                    break;
                case 2:
                    flash.listaErrori.add('Non riesco a cancellare ' + milite + ' Prova a controllare la lista utenti/password')
                    break;
                case 3:
                    flash.listaMessaggi.add('Eliminateo ' + milite + ' dalla lista utenti/password')
                    break;
                default:
                    log.warn("Switch - caso non definito");
                    break;
            } // end of switch statement

            militeInstance.delete(flush: true)
            flash.listaMessaggi.add('Eliminateo ' + milite + ' da tutte le liste')
            // flash.message = message(code: 'default.deleted.message', args: [message(code: 'milite.label', default: 'Milite'), id])

        } catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'milite.label', default: 'Milite'), id])
        }// fine del blocco try-catch

        redirect(action: 'list')
    } // fine del metodo

} // fine della controller classe
