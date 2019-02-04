<script>
    function detectOS() {
        // Code to run when the document is ready.
        var userOS = navigator['platform'] || '';
        // default values
        if(userOS.indexOf("Win") >= 0){
            os = 'windows';
        }
        if(userOS.indexOf("Linux") >= 0){
            os = 'linux';
        }
        if(userOS.indexOf("Mac") >= 0){
            os = 'macos';
        }
        if(os === ''){
            os = 'windows';
        }

        return os;
    }
</script>