/* 
 * Sistemas distribuidos - Practica Hotel
 * Universidad Rey Juan Carlos, Mostoles
 * Realizada por Julio Lopez González y Manuel Gómez Pérez
 * Doble Grado GII + ADE
 * https://github.com/giiade/hotel-distribuido
 */

var formulario = "DNI:";


function myFunction() {
    //formulario = document.getElementById("myform").serialize();
    formulario = $('#myform').serialize();
    alert(formulario);
}
