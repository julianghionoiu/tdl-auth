<#-- @ftlvariable name="SPONSOR" type="java.lang.String" -->
<#-- @ftlvariable name="ENABLE_REPORT_SHARING" type="java.lang.Boolean" -->
<div class="row bottom-border dark-background">
    <div class="col-md-10 container-centered">
        <div>
            <h3>5. Finishing</h3>
            <p>When you have finished, you simply have to stop the recorder (<code>CTRL + C</code> or <code>CTRL +
                D</code>).</p>
            <p>After finishing, your recording will be reviewed by ${SPONSOR} and you will be contacted with
                feedback.</p>

            <#if ENABLE_REPORT_SHARING>
            <p>If you have completed the challenge, ${SPONSOR} will give you access to the performance report and all
                the other solutions. This will be a great opportunity to see how you rank, to see
                how other approaches to the challenge played out.</p>
            </#if>
        </div>
    </div>
</div>