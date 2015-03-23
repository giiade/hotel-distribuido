/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.util.Scanner;

/**
 *
 * @author JulioLopez
 */

public class Cliente {
    private static void consultarHuesped(Scanner teclado){
        System.out.println("Opción 1. Consultar huésped");
        System.out.println("  Introduzca el NIF del huésped que desea consultar"); 
        String nif = teclado.nextLine();
        System.out.println(" PRUEBA NIF =" + nif);
    }
    private static void consultarHuespedApe(Scanner teclado){
        System.out.println("Opción 2. Consultar huésped");
        System.out.println("  Introduzca los apellidos del huésped que desea consultar"); 
        String apellidos = teclado.nextLine();
        System.out.println("PRUEBA APELLIDOS =" + apellidos);
        System.out.println("  Ahora introduzca el nombre del huésped que desea consultar");
        String nombre = teclado.nextLine();
        System.out.println("PRUEBA NOMBRE =" + nombre);
    }
    private static void anadirHuesped(Scanner teclado){
        System.out.println("Opción 3. Añadir huésped");
        System.out.println("  Introduzca el nombre del nuevo huesped"); 
        String nombrehuesped = teclado.nextLine();
        System.out.println("PRUEBA N_Huesped =" + nombrehuesped);
        System.out.println("  Introduzca los apellidos del nuevo huesped"); 
        String apellidoshuesped = teclado.nextLine();
        System.out.println("PRUEBA Ap_Huesped =" + apellidoshuesped);
        System.out.println("  Introduzca el NIF del nuevo huesped"); 
        String nifhuesped = teclado.nextLine();
        System.out.println("PRUEBA Nif_Huesped =" + nifhuesped);
        System.out.println("  Introduzca la dirección del nuevo huesped"); 
        String dirhuesped = teclado.nextLine();
        System.out.println("PRUEBA dir_Huesped =" + dirhuesped);
        System.out.println("  Localidad"); 
        String localhuesped = teclado.nextLine();
        System.out.println("PRUEBA local_Huesped =" + localhuesped);
        System.out.println("  Código postal"); 
        String CPhuesped = teclado.nextLine();
        System.out.println("PRUEBA CP_Huesped =" + CPhuesped);
        System.out.println("  Provincia"); 
        String provinciaHuesped = teclado.nextLine();
        System.out.println("PRUEBA provincia_Huesped =" + provinciaHuesped);
        String check;
        System.out.println("Desea introducir teléfono fijo? S/N");
        check = teclado.nextLine();
        if (check.equals("S")|| check.equals("s")){
            System.out.println("Introduce el número fijo");
            String fijoHuesped = teclado.nextLine();
            System.out.println("PRUEBA fijo_Huesped =" + fijoHuesped);    
        }
        System.out.println("Desea introducir teléfono móvil? S/N");
        check = teclado.nextLine();
        if (check.equals("S")|| check.equals("s")){
            System.out.println("Introduce el número de móvil");
            String movilHuesped = teclado.nextLine();
            System.out.println("PRUEBA movil_Huesped =" + movilHuesped);
        }
        System.out.println("Desea introducir correo electrónico? S/N");
        check = teclado.nextLine();
        if (check.equals("S")|| check.equals("s")){
            System.out.println("Introduce el correo electrónico");
            String correoHuesped = teclado.nextLine();
            System.out.println("PRUEBA correo_Huesped =" + correoHuesped);
        }
        System.out.println("Gracias por introducir los datos del huésped");
    }
    private static void modHuesped(Scanner teclado){
        System.out.println("Opción 4. Modificar datos del huésped");
        System.out.println("  Introduzca el NIF del huésped que desea modificar"); 
        String nif = teclado.nextLine();
        System.out.println(" PRUEBA NIF =" + nif);
    }
    private static void delHuesped(Scanner teclado){
        System.out.println("Opción 5. Eliminar datos del huésped");
        System.out.println("  Introduzca el NIF del huésped que desea eliminar"); 
        String nif = teclado.nextLine();
        System.out.println(" PRUEBA NIF =" + nif);
    }
    private static void buscaReserva(Scanner teclado){
        System.out.println("Opción 6. Buscar reservas por fecha");
                        // FALTA INTRODUCIR FECHAS DE RESERVA
    }
    private static void modReserva(Scanner teclado){
        System.out.println("Opción 7. Modificar reserva");
        System.out.println("  Introduzca el NIF del huésped cuya reserva desea modificar"); 
        String nif = teclado.nextLine();
        System.out.println(" PRUEBA NIF =" + nif);
        // FALTA INTRODUCIR FECHAS DE RESERVA
    }
    private static void delReserva(Scanner teclado){
        System.out.println("Opción 8. Eliminar reserva");
        System.out.println("  Introduzca el NIF del huésped cuya reserva desea eliminar"); 
        String nif = teclado.nextLine();
        System.out.println(" PRUEBA NIF =" + nif);
        // FALTA INTRODUCIR FECHAS DE RESERVA
    }
    private static void anadirReserva(Scanner teclado){
        System.out.println("Opción 9. Añadir reserva");
        System.out.println("  Introduzca el NIF del huésped (ya registrado) a cuyo nombre estará la reserva"); 
        String nif = teclado.nextLine();
        System.out.println(" PRUEBA NIF =" + nif);
    }

    public static void main(String[] args) {
        // TODO code application logic here
        Scanner teclado = new Scanner(System.in);
        int opcion;
        do{
            System.out.println("MENÚ CUTRE DE JULIO");
            System.out.println("    1 - Consultar Huésped (NIF)");
            System.out.println("    2 - Consultar Huésped (Apellidos y nombre)");
            System.out.println("    3 - Añadir Huesped ");
            System.out.println("    4 - Modificar datos del Huésped");
            System.out.println("    5 - Eliminar datos del Huésped");
            System.out.println("    6 - Buscar reservas por fecha");
            System.out.println("    7 - Modificar reserva");
            System.out.println("    8 - Eliminar reserva");
            System.out.println("    9 - Añadir reserva");
            System.out.println("    10 - Salir");
            System.out.println();

            System.out.println("Seleccione el número de la opción deseada");
            opcion = Integer.parseInt(teclado.nextLine());
            switch (opcion) {
                case 1: 
                    consultarHuesped(teclado);
                    break;
                case 2: 
                    consultarHuespedApe(teclado);
                    break;
                case 3: 
                    anadirHuesped(teclado);
                    break;
                case 4: 
                    modHuesped(teclado);
                    break;
                case 5: 
                    delHuesped(teclado);
                    break;
                case 6: 
                    buscaReserva(teclado);
                    break;
                case 7: 
                    modReserva(teclado);
                    break;  
                case 8: 
                    delReserva(teclado);
                    break;
                case 9:
                    anadirReserva(teclado);
                    break;
                case 10:
                    System.out.println("Opción 10. Salir");
                    System.out.println("    Gracias por usar el cutre menú julio para reservas de hotel.");
                    break;    
                default:System.out.println("Opción incorrecta. Por favor, seleccione una válida (1-10)");
            }
        }while (opcion!=10);
    }
}
