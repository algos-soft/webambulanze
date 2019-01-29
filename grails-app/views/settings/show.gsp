
<%@ page import="webambulanze.Settings" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'settings.label', default: 'Settings')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-settings" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-settings" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list settings">
			
				<g:if test="${settingsInstance?.croce}">
				<li class="fieldcontain">
					<span id="croce-label" class="property-label"><g:message code="settings.croce.label" default="Croce" /></span>
					
						<span class="property-value" aria-labelledby="croce-label"><g:link controller="croce" action="show" id="${settingsInstance?.croce?.id}">${settingsInstance?.croce?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.startLogin}">
				<li class="fieldcontain">
					<span id="startLogin-label" class="property-label"><g:message code="settings.startLogin.label" default="Start Login" /></span>
					
						<span class="property-value" aria-labelledby="startLogin-label"><g:formatBoolean boolean="${settingsInstance?.startLogin}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.startController}">
				<li class="fieldcontain">
					<span id="startController-label" class="property-label"><g:message code="settings.startController.label" default="Start Controller" /></span>
					
						<span class="property-value" aria-labelledby="startController-label"><g:fieldValue bean="${settingsInstance}" field="startController"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.allControllers}">
				<li class="fieldcontain">
					<span id="allControllers-label" class="property-label"><g:message code="settings.allControllers.label" default="All Controllers" /></span>
					
						<span class="property-value" aria-labelledby="allControllers-label"><g:formatBoolean boolean="${settingsInstance?.allControllers}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.controlli}">
				<li class="fieldcontain">
					<span id="controlli-label" class="property-label"><g:message code="settings.controlli.label" default="Controlli" /></span>
					
						<span class="property-value" aria-labelledby="controlli-label"><g:fieldValue bean="${settingsInstance}" field="controlli"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.mostraSoloMilitiFunzione}">
				<li class="fieldcontain">
					<span id="mostraSoloMilitiFunzione-label" class="property-label"><g:message code="settings.mostraSoloMilitiFunzione.label" default="Mostra Solo Militi Funzione" /></span>
					
						<span class="property-value" aria-labelledby="mostraSoloMilitiFunzione-label"><g:formatBoolean boolean="${settingsInstance?.mostraSoloMilitiFunzione}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.mostraMilitiFunzioneAndAltri}">
				<li class="fieldcontain">
					<span id="mostraMilitiFunzioneAndAltri-label" class="property-label"><g:message code="settings.mostraMilitiFunzioneAndAltri.label" default="Mostra Militi Funzione And Altri" /></span>
					
						<span class="property-value" aria-labelledby="mostraMilitiFunzioneAndAltri-label"><g:formatBoolean boolean="${settingsInstance?.mostraMilitiFunzioneAndAltri}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.militePuoInserireAltri}">
				<li class="fieldcontain">
					<span id="militePuoInserireAltri-label" class="property-label"><g:message code="settings.militePuoInserireAltri.label" default="Milite Puo Inserire Altri" /></span>
					
						<span class="property-value" aria-labelledby="militePuoInserireAltri-label"><g:formatBoolean boolean="${settingsInstance?.militePuoInserireAltri}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.militePuoModificareAltri}">
				<li class="fieldcontain">
					<span id="militePuoModificareAltri-label" class="property-label"><g:message code="settings.militePuoModificareAltri.label" default="Milite Puo Modificare Altri" /></span>
					
						<span class="property-value" aria-labelledby="militePuoModificareAltri-label"><g:formatBoolean boolean="${settingsInstance?.militePuoModificareAltri}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.militePuoCancellareAltri}">
				<li class="fieldcontain">
					<span id="militePuoCancellareAltri-label" class="property-label"><g:message code="settings.militePuoCancellareAltri.label" default="Milite Puo Cancellare Altri" /></span>
					
						<span class="property-value" aria-labelledby="militePuoCancellareAltri-label"><g:formatBoolean boolean="${settingsInstance?.militePuoCancellareAltri}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.tipoControlloModifica}">
				<li class="fieldcontain">
					<span id="tipoControlloModifica-label" class="property-label"><g:message code="settings.tipoControlloModifica.label" default="Tipo Controllo Modifica" /></span>
					
						<span class="property-value" aria-labelledby="tipoControlloModifica-label"><g:fieldValue bean="${settingsInstance}" field="tipoControlloModifica"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.maxMinutiTrascorsiModifica}">
				<li class="fieldcontain">
					<span id="maxMinutiTrascorsiModifica-label" class="property-label"><g:message code="settings.maxMinutiTrascorsiModifica.label" default="Max Minuti Trascorsi Modifica" /></span>
					
						<span class="property-value" aria-labelledby="maxMinutiTrascorsiModifica-label"><g:fieldValue bean="${settingsInstance}" field="maxMinutiTrascorsiModifica"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.minGiorniMancantiModifica}">
				<li class="fieldcontain">
					<span id="minGiorniMancantiModifica-label" class="property-label"><g:message code="settings.minGiorniMancantiModifica.label" default="Min Giorni Mancanti Modifica" /></span>
					
						<span class="property-value" aria-labelledby="minGiorniMancantiModifica-label"><g:fieldValue bean="${settingsInstance}" field="minGiorniMancantiModifica"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.tipoControlloCancellazione}">
				<li class="fieldcontain">
					<span id="tipoControlloCancellazione-label" class="property-label"><g:message code="settings.tipoControlloCancellazione.label" default="Tipo Controllo Cancellazione" /></span>
					
						<span class="property-value" aria-labelledby="tipoControlloCancellazione-label"><g:fieldValue bean="${settingsInstance}" field="tipoControlloCancellazione"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.maxMinutiTrascorsiCancellazione}">
				<li class="fieldcontain">
					<span id="maxMinutiTrascorsiCancellazione-label" class="property-label"><g:message code="settings.maxMinutiTrascorsiCancellazione.label" default="Max Minuti Trascorsi Cancellazione" /></span>
					
						<span class="property-value" aria-labelledby="maxMinutiTrascorsiCancellazione-label"><g:fieldValue bean="${settingsInstance}" field="maxMinutiTrascorsiCancellazione"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.minGiorniMancantiCancellazione}">
				<li class="fieldcontain">
					<span id="minGiorniMancantiCancellazione-label" class="property-label"><g:message code="settings.minGiorniMancantiCancellazione.label" default="Min Giorni Mancanti Cancellazione" /></span>
					
						<span class="property-value" aria-labelledby="minGiorniMancantiCancellazione-label"><g:fieldValue bean="${settingsInstance}" field="minGiorniMancantiCancellazione"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.isOrarioTurnoModificabileForm}">
				<li class="fieldcontain">
					<span id="isOrarioTurnoModificabileForm-label" class="property-label"><g:message code="settings.isOrarioTurnoModificabileForm.label" default="Is Orario Turno Modificabile Form" /></span>
					
						<span class="property-value" aria-labelledby="isOrarioTurnoModificabileForm-label"><g:formatBoolean boolean="${settingsInstance?.isOrarioTurnoModificabileForm}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.isCalcoloNotturnoStatistiche}">
				<li class="fieldcontain">
					<span id="isCalcoloNotturnoStatistiche-label" class="property-label"><g:message code="settings.isCalcoloNotturnoStatistiche.label" default="Is Calcolo Notturno Statistiche" /></span>
					
						<span class="property-value" aria-labelledby="isCalcoloNotturnoStatistiche-label"><g:formatBoolean boolean="${settingsInstance?.isCalcoloNotturnoStatistiche}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.isDisabilitazioneAutomaticaLogin}">
				<li class="fieldcontain">
					<span id="isDisabilitazioneAutomaticaLogin-label" class="property-label"><g:message code="settings.isDisabilitazioneAutomaticaLogin.label" default="Is Disabilitazione Automatica Login" /></span>
					
						<span class="property-value" aria-labelledby="isDisabilitazioneAutomaticaLogin-label"><g:formatBoolean boolean="${settingsInstance?.isDisabilitazioneAutomaticaLogin}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.fissaLimiteMassimoSingoloTurno}">
				<li class="fieldcontain">
					<span id="fissaLimiteMassimoSingoloTurno-label" class="property-label"><g:message code="settings.fissaLimiteMassimoSingoloTurno.label" default="Fissa Limite Massimo Singolo Turno" /></span>
					
						<span class="property-value" aria-labelledby="fissaLimiteMassimoSingoloTurno-label"><g:formatBoolean boolean="${settingsInstance?.fissaLimiteMassimoSingoloTurno}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.oreMassimeSingoloTurno}">
				<li class="fieldcontain">
					<span id="oreMassimeSingoloTurno-label" class="property-label"><g:message code="settings.oreMassimeSingoloTurno.label" default="Ore Massime Singolo Turno" /></span>
					
						<span class="property-value" aria-labelledby="oreMassimeSingoloTurno-label"><g:fieldValue bean="${settingsInstance}" field="oreMassimeSingoloTurno"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.usaModuloViaggi}">
				<li class="fieldcontain">
					<span id="usaModuloViaggi-label" class="property-label"><g:message code="settings.usaModuloViaggi.label" default="Usa Modulo Viaggi" /></span>
					
						<span class="property-value" aria-labelledby="usaModuloViaggi-label"><g:formatBoolean boolean="${settingsInstance?.usaModuloViaggi}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.numeroServiziEffettuati}">
				<li class="fieldcontain">
					<span id="numeroServiziEffettuati-label" class="property-label"><g:message code="settings.numeroServiziEffettuati.label" default="Numero Servizi Effettuati" /></span>
					
						<span class="property-value" aria-labelledby="numeroServiziEffettuati-label"><g:fieldValue bean="${settingsInstance}" field="numeroServiziEffettuati"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.tabelloneSecured}">
				<li class="fieldcontain">
					<span id="tabelloneSecured-label" class="property-label"><g:message code="settings.tabelloneSecured.label" default="Tabellone Secured" /></span>
					
						<span class="property-value" aria-labelledby="tabelloneSecured-label"><g:formatBoolean boolean="${settingsInstance?.tabelloneSecured}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.turniSecured}">
				<li class="fieldcontain">
					<span id="turniSecured-label" class="property-label"><g:message code="settings.turniSecured.label" default="Turni Secured" /></span>
					
						<span class="property-value" aria-labelledby="turniSecured-label"><g:formatBoolean boolean="${settingsInstance?.turniSecured}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.mostraTabellonePartenza}">
				<li class="fieldcontain">
					<span id="mostraTabellonePartenza-label" class="property-label"><g:message code="settings.mostraTabellonePartenza.label" default="Mostra Tabellone Partenza" /></span>
					
						<span class="property-value" aria-labelledby="mostraTabellonePartenza-label"><g:formatBoolean boolean="${settingsInstance?.mostraTabellonePartenza}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.bloccaSoloFunzioniObbligatorie}">
				<li class="fieldcontain">
					<span id="bloccaSoloFunzioniObbligatorie-label" class="property-label"><g:message code="settings.bloccaSoloFunzioniObbligatorie.label" default="Blocca Solo Funzioni Obbligatorie" /></span>
					
						<span class="property-value" aria-labelledby="bloccaSoloFunzioniObbligatorie-label"><g:formatBoolean boolean="${settingsInstance?.bloccaSoloFunzioniObbligatorie}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.militePuoCreareTurnoStandard}">
				<li class="fieldcontain">
					<span id="militePuoCreareTurnoStandard-label" class="property-label"><g:message code="settings.militePuoCreareTurnoStandard.label" default="Milite Puo Creare Turno Standard" /></span>
					
						<span class="property-value" aria-labelledby="militePuoCreareTurnoStandard-label"><g:formatBoolean boolean="${settingsInstance?.militePuoCreareTurnoStandard}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.militePuoCreareTurnoExtra}">
				<li class="fieldcontain">
					<span id="militePuoCreareTurnoExtra-label" class="property-label"><g:message code="settings.militePuoCreareTurnoExtra.label" default="Milite Puo Creare Turno Extra" /></span>
					
						<span class="property-value" aria-labelledby="militePuoCreareTurnoExtra-label"><g:formatBoolean boolean="${settingsInstance?.militePuoCreareTurnoExtra}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.isTurnoSettimanale}">
				<li class="fieldcontain">
					<span id="isTurnoSettimanale-label" class="property-label"><g:message code="settings.isTurnoSettimanale.label" default="Is Turno Settimanale" /></span>
					
						<span class="property-value" aria-labelledby="isTurnoSettimanale-label"><g:formatBoolean boolean="${settingsInstance?.isTurnoSettimanale}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.usaNomeCognome}">
				<li class="fieldcontain">
					<span id="usaNomeCognome-label" class="property-label"><g:message code="settings.usaNomeCognome.label" default="Usa Nome Cognome" /></span>
					
						<span class="property-value" aria-labelledby="usaNomeCognome-label"><g:formatBoolean boolean="${settingsInstance?.usaNomeCognome}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.usaListaMilitiViaggi}">
				<li class="fieldcontain">
					<span id="usaListaMilitiViaggi-label" class="property-label"><g:message code="settings.usaListaMilitiViaggi.label" default="Usa Lista Militi Viaggi" /></span>
					
						<span class="property-value" aria-labelledby="usaListaMilitiViaggi-label"><g:formatBoolean boolean="${settingsInstance?.usaListaMilitiViaggi}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.suggerisceKilometroViaggio}">
				<li class="fieldcontain">
					<span id="suggerisceKilometroViaggio-label" class="property-label"><g:message code="settings.suggerisceKilometroViaggio.label" default="Suggerisce Kilometro Viaggio" /></span>
					
						<span class="property-value" aria-labelledby="suggerisceKilometroViaggio-label"><g:formatBoolean boolean="${settingsInstance?.suggerisceKilometroViaggio}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.militePuoCreareTurnoImmediato}">
				<li class="fieldcontain">
					<span id="militePuoCreareTurnoImmediato-label" class="property-label"><g:message code="settings.militePuoCreareTurnoImmediato.label" default="Milite Puo Creare Turno Immediato" /></span>
					
						<span class="property-value" aria-labelledby="militePuoCreareTurnoImmediato-label"><g:formatBoolean boolean="${settingsInstance?.militePuoCreareTurnoImmediato}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:settingsInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${settingsInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
