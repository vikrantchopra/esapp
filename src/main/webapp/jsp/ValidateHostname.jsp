<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags" %>
<script type="text/javascript">

    function validateAndSubmit() {
        var errorMessage = '';
        

        if ($.trim(errorMessage) != '') {
            alert(errorMessage);
            return false;
        }
        else {
            $("#id_INRequest").submit();
            return true;
        }
    }
</script>
<table width="95%" border="0" cellspacing="10" cellpadding="0" align="center">
    <tr>
        <td width="100%" class="box_border" colspan="3">
            <table align="center" width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <th colspan="40%" align="left" valign="top" class="table_top">Validate Hostname for ImageNow License
                    </th>
                    <th colspan="60%" align="left" valign="top" class="table_top">&nbsp;</th>
                </tr>

                <tr>
                    <td colspan="2" class="body_text">
                        <!--Content goes here--->
                        <s:form id="id_INRequest" action="onHostnameValidationAction" theme="simple"
                                enctype="multipart/form-data">
                            <s:token/>
                            <table>
                                <s:if test="%{errorMessage!=null && errorMessage!=''}">
                                    <tr>
                                        <td colspan="2"><span style="color: red;"> <s:property
                                                value="errorMessage"/></span></td>
                                    </tr>
                                </s:if>
                                
                                <%-- <tr>
                                    <td>Number Of Licenses</td>
                                    <td><s:textfield name="imagenowlicenses.numberOfLicenses"
                                                     id="id_INNumberOfLicense" theme="simple"
                                                     cssClass="body_text"/></td>
                                </tr> --%>
                                <tr>
                                    <td>Validate Hostname / IP</td>
                                    <td><s:textfield name="imagenowlicenses.hostname"
                                                     id="id_Hostname" theme="simple"
                                                     cssClass="body_text"/></td>
                                </tr>
                                
                                
                                <tr>
                                    <td align="right">
                                        <div align="right" class="btn_bg"><a href="#"
                                                                             onclick="return validateAndSubmit()">Validate</a>
                                        </div>
                                    </td>
                                    <td align="left">
                                        <div align="right" class="btn_bg"><a href="#"
                                                                             onclick="$('#id_INRequest').get(0).reset()">Reset</a>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </s:form>
                        <!--Content goes here--->
                    </td>
                </tr>

            </table>
        </td>
    </tr>
</table>
<script type="text/javascript">
    $("input").keypress(function (event) {
        if (event.which == 13) {
            event.preventDefault();
            validateAndSubmit();
        }
    });
</script>