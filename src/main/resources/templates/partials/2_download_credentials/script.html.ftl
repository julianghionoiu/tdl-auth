<#-- @ftlvariable name="EXPIRATION_DATE" type="java.lang.Long" -->
<script>
    $(document).ready(function(){
        var expDate = ${EXPIRATION_DATE};
        var timeNow = new Date();
        if (timeNow >= expDate){
            $('#download_credentials').attr('disabled', 'disabled');
            $('.my-alert-danger').css('display', 'inline-block');
        }
    });
</script>