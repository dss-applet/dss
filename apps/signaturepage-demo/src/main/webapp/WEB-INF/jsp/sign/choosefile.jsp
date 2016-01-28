<%@ taglib prefix="form" tagdir="/WEB-INF/tags/form" %>
<%@ taglib prefix="app" tagdir="/WEB-INF/tags" %>
<div class="fluid-column fluid-c6">
<form:section descr="Please select the PDF you want to sign" name="choosefile">
		<form action="choosefile.html" method="post" id ="chooseFileForm" enctype="multipart/form-data">
			<input type="file" id ="file" name="file" onchange="submitForm();">
		</form>
</form:section>
</div>
<script type="text/javascript">

	function submitForm(){
		$('#chooseFileForm').submit();
	}

</script>
