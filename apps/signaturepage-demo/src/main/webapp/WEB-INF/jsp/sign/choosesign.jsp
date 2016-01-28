<%@ taglib prefix="form" tagdir="/WEB-INF/tags/form" %>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="bean" tagdir="/WEB-INF/tags/bean" %>

<div class="fluid-column fluid-c6">
<form:section descr="dss_applet_sel_methods_desc" name="choosemethod">
		<form action="choosesign.html" method="post" id ="chooseSignForm">
			<input type="radio" id ="sign_sc" name="SM" value="sc" onclick="submitForm();"><label for="sign_sc"><bean:message key="dss_applet_message_method_sc" bundle="none"/></label><br>
			<input type="radio" id ="sign_ct" name="SM" value="installed_cert" onclick="submitForm();"><label for="sign_ct"><bean:message key="dss_applet_message_method_installed_cert" bundle="none"/></label><br>
			<input type="radio" id ="sign_p12" name="SM" value="p12" onclick="submitForm();"><label for="sign_p12"><bean:message key="dss_applet_message_method_p12" bundle="none"/></label>
		</form>
</form:section>
</div>
<script type="text/javascript">

	function submitForm(){
		$('#chooseSignForm').submit();
	}

</script>
