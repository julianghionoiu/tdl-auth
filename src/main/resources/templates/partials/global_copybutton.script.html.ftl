<script>
    function copytext(event) {
        event.preventDefault();

        var target = event.target;
        var parentDiv = $(target).parents('div.copy-block');
        var codeText = parentDiv.find('code.copy-code').text();

        var $temp = $("<input>");
        $("body").append($temp);
        $temp.val(codeText).select();
        document.execCommand("copy");
        $temp.remove();
    }

    $('.copy-text').click(copytext);
</script>