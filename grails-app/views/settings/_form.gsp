<%@ page import="webambulanze.Settings" %>



<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'croce', 'error')} required">
	<label for="croce">
		<g:message code="settings.croce.label" default="Croce" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="croce" name="croce.id" from="${webambulanze.Croce.list()}" optionKey="id" required="" value="${settingsInstance?.croce?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'startLogin', 'error')} ">
	<label for="startLogin">
		<g:message code="settings.startLogin.label" default="Start Login" />
		
	</label>
	<g:checkBox name="startLogin" value="${settingsInstance?.startLogin}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'startController', 'error')} required">
	<label for="startController">
		<g:message code="settings.startController.label" default="Start Controller" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="startController" required="" value="${settingsInstance?.startController}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'allControllers', 'error')} ">
	<label for="allControllers">
		<g:message code="settings.allControllers.label" default="All Controllers" />
		
	</label>
	<g:checkBox name="allControllers" value="${settingsInstance?.allControllers}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'controlli', 'error')} ">
	<label for="controlli">
		<g:message code="settings.controlli.label" default="Controlli" />
		
	</label>
	<g:textField name="controlli" value="${settingsInstance?.controlli}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'mostraSoloMilitiFunzione', 'error')} ">
	<label for="mostraSoloMilitiFunzione">
		<g:message code="settings.mostraSoloMilitiFunzione.label" default="Mostra Solo Militi Funzione" />
		
	</label>
	<g:checkBox name="mostraSoloMilitiFunzione" value="${settingsInstance?.mostraSoloMilitiFunzione}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'mostraMilitiFunzioneAndAltri', 'error')} ">
	<label for="mostraMilitiFunzioneAndAltri">
		<g:message code="settings.mostraMilitiFunzioneAndAltri.label" default="Mostra Militi Funzione And Altri" />
		
	</label>
	<g:checkBox name="mostraMilitiFunzioneAndAltri" value="${settingsInstance?.mostraMilitiFunzioneAndAltri}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'militePuoInserireAltri', 'error')} ">
	<label for="militePuoInserireAltri">
		<g:message code="settings.militePuoInserireAltri.label" default="Milite Puo Inserire Altri" />
		
	</label>
	<g:checkBox name="militePuoInserireAltri" value="${settingsInstance?.militePuoInserireAltri}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'militePuoModificareAltri', 'error')} ">
	<label for="militePuoModificareAltri">
		<g:message code="settings.militePuoModificareAltri.label" default="Milite Puo Modificare Altri" />
		
	</label>
	<g:checkBox name="militePuoModificareAltri" value="${settingsInstance?.militePuoModificareAltri}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'militePuoCancellareAltri', 'error')} ">
	<label for="militePuoCancellareAltri">
		<g:message code="settings.militePuoCancellareAltri.label" default="Milite Puo Cancellare Altri" />
		
	</label>
	<g:checkBox name="militePuoCancellareAltri" value="${settingsInstance?.militePuoCancellareAltri}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'tipoControlloModifica', 'error')} required">
	<label for="tipoControlloModifica">
		<g:message code="settings.tipoControlloModifica.label" default="Tipo Controllo Modifica" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="tipoControlloModifica" from="${webambulanze.ControlloTemporale?.values()}" keys="${webambulanze.ControlloTemporale.values()*.name()}" required="" value="${settingsInstance?.tipoControlloModifica?.name()}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'maxMinutiTrascorsiModifica', 'error')} required">
	<label for="maxMinutiTrascorsiModifica">
		<g:message code="settings.maxMinutiTrascorsiModifica.label" default="Max Minuti Trascorsi Modifica" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="maxMinutiTrascorsiModifica" type="number" value="${settingsInstance.maxMinutiTrascorsiModifica}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'minGiorniMancantiModifica', 'error')} required">
	<label for="minGiorniMancantiModifica">
		<g:message code="settings.minGiorniMancantiModifica.label" default="Min Giorni Mancanti Modifica" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="minGiorniMancantiModifica" type="number" value="${settingsInstance.minGiorniMancantiModifica}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'tipoControlloCancellazione', 'error')} required">
	<label for="tipoControlloCancellazione">
		<g:message code="settings.tipoControlloCancellazione.label" default="Tipo Controllo Cancellazione" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="tipoControlloCancellazione" from="${webambulanze.ControlloTemporale?.values()}" keys="${webambulanze.ControlloTemporale.values()*.name()}" required="" value="${settingsInstance?.tipoControlloCancellazione?.name()}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'maxMinutiTrascorsiCancellazione', 'error')} required">
	<label for="maxMinutiTrascorsiCancellazione">
		<g:message code="settings.maxMinutiTrascorsiCancellazione.label" default="Max Minuti Trascorsi Cancellazione" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="maxMinutiTrascorsiCancellazione" type="number" value="${settingsInstance.maxMinutiTrascorsiCancellazione}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'minGiorniMancantiCancellazione', 'error')} required">
	<label for="minGiorniMancantiCancellazione">
		<g:message code="settings.minGiorniMancantiCancellazione.label" default="Min Giorni Mancanti Cancellazione" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="minGiorniMancantiCancellazione" type="number" value="${settingsInstance.minGiorniMancantiCancellazione}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'isOrarioTurnoModificabileForm', 'error')} ">
	<label for="isOrarioTurnoModificabileForm">
		<g:message code="settings.isOrarioTurnoModificabileForm.label" default="Is Orario Turno Modificabile Form" />
		
	</label>
	<g:checkBox name="isOrarioTurnoModificabileForm" value="${settingsInstance?.isOrarioTurnoModificabileForm}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'isCalcoloNotturnoStatistiche', 'error')} ">
	<label for="isCalcoloNotturnoStatistiche">
		<g:message code="settings.isCalcoloNotturnoStatistiche.label" default="Is Calcolo Notturno Statistiche" />
		
	</label>
	<g:checkBox name="isCalcoloNotturnoStatistiche" value="${settingsInstance?.isCalcoloNotturnoStatistiche}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'isDisabilitazioneAutomaticaLogin', 'error')} ">
	<label for="isDisabilitazioneAutomaticaLogin">
		<g:message code="settings.isDisabilitazioneAutomaticaLogin.label" default="Is Disabilitazione Automatica Login" />
		
	</label>
	<g:checkBox name="isDisabilitazioneAutomaticaLogin" value="${settingsInstance?.isDisabilitazioneAutomaticaLogin}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'fissaLimiteMassimoSingoloTurno', 'error')} ">
	<label for="fissaLimiteMassimoSingoloTurno">
		<g:message code="settings.fissaLimiteMassimoSingoloTurno.label" default="Fissa Limite Massimo Singolo Turno" />
		
	</label>
	<g:checkBox name="fissaLimiteMassimoSingoloTurno" value="${settingsInstance?.fissaLimiteMassimoSingoloTurno}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'oreMassimeSingoloTurno', 'error')} required">
	<label for="oreMassimeSingoloTurno">
		<g:message code="settings.oreMassimeSingoloTurno.label" default="Ore Massime Singolo Turno" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="oreMassimeSingoloTurno" type="number" value="${settingsInstance.oreMassimeSingoloTurno}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'usaModuloViaggi', 'error')} ">
	<label for="usaModuloViaggi">
		<g:message code="settings.usaModuloViaggi.label" default="Usa Modulo Viaggi" />
		
	</label>
	<g:checkBox name="usaModuloViaggi" value="${settingsInstance?.usaModuloViaggi}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'numeroServiziEffettuati', 'error')} required">
	<label for="numeroServiziEffettuati">
		<g:message code="settings.numeroServiziEffettuati.label" default="Numero Servizi Effettuati" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="numeroServiziEffettuati" type="number" value="${settingsInstance.numeroServiziEffettuati}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'tabelloneSecured', 'error')} ">
	<label for="tabelloneSecured">
		<g:message code="settings.tabelloneSecured.label" default="Tabellone Secured" />
		
	</label>
	<g:checkBox name="tabelloneSecured" value="${settingsInstance?.tabelloneSecured}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'turniSecured', 'error')} ">
	<label for="turniSecured">
		<g:message code="settings.turniSecured.label" default="Turni Secured" />
		
	</label>
	<g:checkBox name="turniSecured" value="${settingsInstance?.turniSecured}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'mostraTabellonePartenza', 'error')} ">
	<label for="mostraTabellonePartenza">
		<g:message code="settings.mostraTabellonePartenza.label" default="Mostra Tabellone Partenza" />
		
	</label>
	<g:checkBox name="mostraTabellonePartenza" value="${settingsInstance?.mostraTabellonePartenza}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'bloccaSoloFunzioniObbligatorie', 'error')} ">
	<label for="bloccaSoloFunzioniObbligatorie">
		<g:message code="settings.bloccaSoloFunzioniObbligatorie.label" default="Blocca Solo Funzioni Obbligatorie" />
		
	</label>
	<g:checkBox name="bloccaSoloFunzioniObbligatorie" value="${settingsInstance?.bloccaSoloFunzioniObbligatorie}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'militePuoCreareTurnoStandard', 'error')} ">
	<label for="militePuoCreareTurnoStandard">
		<g:message code="settings.militePuoCreareTurnoStandard.label" default="Milite Puo Creare Turno Standard" />
		
	</label>
	<g:checkBox name="militePuoCreareTurnoStandard" value="${settingsInstance?.militePuoCreareTurnoStandard}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'militePuoCreareTurnoExtra', 'error')} ">
	<label for="militePuoCreareTurnoExtra">
		<g:message code="settings.militePuoCreareTurnoExtra.label" default="Milite Puo Creare Turno Extra" />
		
	</label>
	<g:checkBox name="militePuoCreareTurnoExtra" value="${settingsInstance?.militePuoCreareTurnoExtra}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'isTurnoSettimanale', 'error')} ">
	<label for="isTurnoSettimanale">
		<g:message code="settings.isTurnoSettimanale.label" default="Is Turno Settimanale" />
		
	</label>
	<g:checkBox name="isTurnoSettimanale" value="${settingsInstance?.isTurnoSettimanale}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'usaNomeCognome', 'error')} ">
	<label for="usaNomeCognome">
		<g:message code="settings.usaNomeCognome.label" default="Usa Nome Cognome" />
		
	</label>
	<g:checkBox name="usaNomeCognome" value="${settingsInstance?.usaNomeCognome}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'usaListaMilitiViaggi', 'error')} ">
	<label for="usaListaMilitiViaggi">
		<g:message code="settings.usaListaMilitiViaggi.label" default="Usa Lista Militi Viaggi" />
		
	</label>
	<g:checkBox name="usaListaMilitiViaggi" value="${settingsInstance?.usaListaMilitiViaggi}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'suggerisceKilometroViaggio', 'error')} ">
	<label for="suggerisceKilometroViaggio">
		<g:message code="settings.suggerisceKilometroViaggio.label" default="Suggerisce Kilometro Viaggio" />
		
	</label>
	<g:checkBox name="suggerisceKilometroViaggio" value="${settingsInstance?.suggerisceKilometroViaggio}" />

</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'militePuoCreareTurnoImmediato', 'error')} ">
	<label for="militePuoCreareTurnoImmediato">
		<g:message code="settings.militePuoCreareTurnoImmediato.label" default="Milite Puo Creare Turno Immediato" />
		
	</label>
	<g:checkBox name="militePuoCreareTurnoImmediato" value="${settingsInstance?.militePuoCreareTurnoImmediato}" />

</div>

