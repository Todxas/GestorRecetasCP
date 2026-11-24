public class Receta {

    private int id;
    private String nombre;
    private String categoria;
    private int tiempoPreparacion;
    private String dificultad;
    private int raciones;

    public Receta(int id, String nombre, String categoria, int tiempoPreparacion, String dificultad, int raciones) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.tiempoPreparacion = tiempoPreparacion;
        this.dificultad = dificultad;
        this.raciones = raciones;
    }

    public Receta(String nombre, String categoria, int tiempoPreparacion, String dificultad, int raciones) {
        this(0, nombre, categoria, tiempoPreparacion, dificultad, raciones);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public String getDificultad() {
        return dificultad;
    }

    public int getRaciones() {
        return raciones;
    }

    @Override
    public String toString() {
        return id + " - " + nombre + " | " + categoria + " | " + tiempoPreparacion + " minutos" + " | " + dificultad + " | " + raciones + " raciones";
    }
}
