import java.sql.*;
import java.util.Scanner;

public class GestionRecetas {
    static Scanner teclado = new Scanner(System.in);
    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("--- GESTOR DE RECETAS ---");
            System.out.println("1. Mostrar recetas");
            System.out.println("2. Buscar receta por ID");
            System.out.println("3. Añadir receta");
            System.out.println("4. Modificar receta");
            System.out.println("5. Eliminar receta");
            System.out.println("6. Añadir receta con transacción");
            System.out.println("7. Buscar receta por nombre");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = teclado.nextInt();
            switch (opcion) {
                case 1 -> mostrarRecetas();
                case 2 -> buscarPorId();
                case 3 -> anadirReceta();
                case 4 -> modificarReceta();
                case 5 -> eliminarReceta();
                case 6 -> anadirConTransaccion();
                case 7 -> buscarPorNombre();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción incorrecta.");
            }
        }
        while (opcion != 0);
    }

    public static void mostrarRecetas() {
        String sql = "SELECT * FROM recetas";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Receta r = new Receta(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getInt("tiempo_preparacion"),
                        rs.getString("dificultad"),
                        rs.getInt("raciones")
                );
                System.out.println(r);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void buscarPorId() {
        System.out.print("Introduce el ID: ");
        int id = teclado.nextInt();
        String sql = "SELECT * FROM recetas WHERE id = ?";
        try (Connection con = ConexionBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Categoría: " + rs.getString("categoria"));
                System.out.println("Tiempo: " + rs.getInt("tiempo_preparacion"));
                System.out.println("Dificultad: " + rs.getString("dificultad"));
                System.out.println("Raciones: " + rs.getInt("raciones"));
            }
            else {
                System.out.println("Receta no encontrada.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void anadirReceta() {
        teclado.nextLine();
        System.out.print("Nombre: ");
        String nombre = teclado.nextLine();
        System.out.print("Categoría: ");
        String categoria = teclado.nextLine();
        System.out.print("Tiempo de preparación: ");
        int tiempo = teclado.nextInt();
        teclado.nextLine();
        System.out.print("Dificultad: ");
        String dificultad = teclado.nextLine();
        System.out.print("Raciones: ");
        int raciones = teclado.nextInt();
        String sql = "INSERT INTO recetas " + "(nombre, categoria, tiempo_preparacion, dificultad, raciones) " + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, categoria);
            ps.setInt(3, tiempo);
            ps.setString(4, dificultad);
            ps.setInt(5, raciones);
            ps.executeUpdate();
            System.out.println("Receta añadida.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void modificarReceta() {
        System.out.print("ID de la receta: ");
        int id = teclado.nextInt();
        System.out.print("Nuevo tiempo: ");
        int tiempo = teclado.nextInt();
        System.out.print("Nuevas raciones: ");
        int raciones = teclado.nextInt();
        String sql = "UPDATE recetas SET tiempo_preparacion = ?, " + "raciones = ? WHERE id = ?";
        try (Connection con = ConexionBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, tiempo);
            ps.setInt(2, raciones);
            ps.setInt(3, id);
            int filas = ps.executeUpdate();
            System.out.println(filas > 0 ? "Receta modificada." : "Receta no encontrada.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void eliminarReceta() {
        System.out.print("ID de la receta: ");
        int id = teclado.nextInt();
        String sql = "DELETE FROM recetas WHERE id = ?";
        try (Connection con = ConexionBD.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            System.out.println(filas > 0 ? "Receta eliminada." : "Receta no encontrada.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void anadirConTransaccion() {
        teclado.nextLine();
        System.out.print("Nombre: ");
        String nombre = teclado.nextLine();
        System.out.print("Categoría: ");
        String categoria = teclado.nextLine();
        System.out.print("Tiempo: ");
        int tiempo = teclado.nextInt();
        teclado.nextLine();
        System.out.print("Dificultad: ");
        String dificultad = teclado.nextLine();
        System.out.print("Raciones: ");
        int raciones = teclado.nextInt();
        String sqlReceta = "INSERT INTO recetas " + "(nombre, categoria, tiempo_preparacion, dificultad, raciones) " + "VALUES (?, ?, ?, ?, ?)";
        String sqlEstadisticas = "UPDATE estadisticas SET total_recetas = total_recetas + 1 WHERE id = 1";
        try (Connection con = ConexionBD.conectar()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps1 = con.prepareStatement(sqlReceta);
                 PreparedStatement ps2 = con.prepareStatement(sqlEstadisticas)) {
                ps1.setString(1, nombre);
                ps1.setString(2, categoria);
                ps1.setInt(3, tiempo);
                ps1.setString(4, dificultad);
                ps1.setInt(5, raciones);
                ps1.executeUpdate();
                ps2.executeUpdate();
                con.commit();
                System.out.println("Receta añadida y estadísticas actualizadas.");
            } catch (SQLException e) {
                con.rollback();
                System.out.println("Error. Se deshacen los cambios.");
            }

        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }

    public static void buscarPorNombre() {
        teclado.nextLine();
        System.out.print("Nombre de la receta: ");
        String nombre = teclado.nextLine();
        try (Connection con = ConexionBD.conectar();
             CallableStatement cs = con.prepareCall("{CALL buscarReceta(?)}")) {
            cs.setString(1, nombre);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Categoría: " + rs.getString("categoria"));
                System.out.println("Tiempo: " + rs.getInt("tiempo_preparacion"));
                System.out.println("Dificultad: " + rs.getString("dificultad"));
                System.out.println("Raciones: " + rs.getInt("raciones"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
