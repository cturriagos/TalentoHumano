/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//<![CDATA[
        function dateTemplateFunc(date) {
            return '<span style="background-color:' + ((date.day < 21 && date.day > 10) ? '#81C784' : 'inherit') + ';border-radius:50%;padding: .25em;width: 1.75em; height:1.75em; display:block;">' + date.day + '</span>';
        }

//]]>
/*document.getElementById('email').addEventListener('input', function() {
    campo = event.target;
    valido = document.getElementById('emailOK');
        
    emailRegex = /^[-\w.%+]{1,64}@(?:[A-Z0-9-]{1,63}\.){1,125}[A-Z]{2,63}$/i;
    //Se muestra un texto a modo de ejemplo, luego va a ser un icono
    if (emailRegex.test(campo.value)) {
      valido.innerText = "válido";
    } else {
      valido.innerText = "incorrecto";
    }
});*/