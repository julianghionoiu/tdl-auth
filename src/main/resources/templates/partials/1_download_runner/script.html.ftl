<script>
    $(document).ready(function(){
        var osList = $('.os-list');

        var downloadTabContentDiv = $('#download');
        var downloadTabContentDivCode = downloadTabContentDiv.find('code.copy-code');
        var downloadTabContentDivCodeText = downloadTabContentDivCode.text();
        var downloadTabContentDivLink = downloadTabContentDiv.find('a.download-link');
        var downloadTabContentDivLinkHref = downloadTabContentDivLink.attr('href');

        var unzipEl = $('#unzip');
        var unzipCodeEl = unzipEl.find('code.copy-code');
        var unzipElText = unzipCodeEl.text();

        var os = detectOS();

        $.each(osList, function( index, value ) {
            var hrefValue = $(value).find('a').attr('href').split('#')[1];

            if(hrefValue === os){
                $(value).find('a').addClass('active');
            }
        });
        
        downloadTabContentDivCodeText = downloadTabContentDivCodeText.replace(/windows/g, os);
        downloadTabContentDivCode.text(downloadTabContentDivCodeText);

        downloadTabContentDivLinkHref = downloadTabContentDivLinkHref.replace(/windows/g, os);
        downloadTabContentDivLink.attr('href', downloadTabContentDivLinkHref);

        unzipElText = unzipElText.replace('windows', os);
        unzipCodeEl.text(unzipElText);

        // if you click on os button
        osList.click(function (event) {
            event.preventDefault();

            var target = event.target;
            var targetHrefValue = $(target).attr('href').split('#')[1];

            // default values are changed to particular os
            downloadTabContentDivCodeText = replaceOSText(downloadTabContentDivCodeText, targetHrefValue);
            downloadTabContentDivCode.text(downloadTabContentDivCodeText);

            downloadTabContentDivLinkHref = replaceOSText(downloadTabContentDivLinkHref, targetHrefValue);
            downloadTabContentDivLink.attr('href', downloadTabContentDivLinkHref);

            unzipElText = replaceOSText(unzipElText, targetHrefValue);
            unzipCodeEl.text(unzipElText);
        });

        function replaceOSText(text, newText){
            text = text.replace(/linux/g, newText);
            text = text.replace(/windows/g, newText);
            text = text.replace(/macos/g, newText);

            return text;
        }

        // if you click on languages button
        $('#languagesTab').on('click', '.nav-link', function (event) {
            event.preventDefault();

            var target = event.target;
            var targetHrefValue = $(target).attr('href').split('#')[1];

            downloadTabContentDivCodeText = changeText(downloadTabContentDivCodeText, targetHrefValue);
            downloadTabContentDivCode.text(downloadTabContentDivCodeText);

            downloadTabContentDivLinkHref = changeText(downloadTabContentDivLinkHref, targetHrefValue);
            downloadTabContentDivLink.attr('href', downloadTabContentDivLinkHref);

            unzipElText = changeText(unzipElText, targetHrefValue);
            unzipCodeEl.text(unzipElText);
        });

        function changeText(text, newText){
            var textArr = text.split('-');
            textArr[2] = newText;
            text = textArr.join('-');

            return text;
        }
    });
</script>