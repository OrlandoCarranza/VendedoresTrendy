/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sucursalesupdatesavingmodified;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Orlando Carranza
 */
public class Vendedores extends javax.swing.JFrame {

    static int insertarVendedor;
    static int listarVendedores;
    private Connection c = null;
    private ResultSet rs = null;
    private ResultSet queryVendedor = null;
    private ResultSet queryEmpleados = null;
    private ResultSet queryBuscaryModificarEmpleado = null;
    private Statement stmt = null;
    private ResultSetMetaData rsm;
    int contarID = 0;
    int identificador;
    int PuestoVendedor_id;
    String NombreVendedor;
    String Apaterno;
    String Amaterno;
    String Calle;
    String Exterior;
    String Interior;
    String Colonia;
    String Municipio;
    String Estado;
    String Pais;
    String CodigoPostal;
    String Email;
    String Telefono;
    String Celular;
    String ClaveVendedor;
    String PuestoVendedor;
    String FechaIngreso;
    String FechaBaja;
    Date Fecha1;
    Date Fecha2;
    String datos[][] = {};
    String campos[] = {"Nombre", "Apellido Paterno", "Puesto", "Clave de Vendedor"};
    int obtenerClaveVendedor = 0;
    String obtenerNombre = "";
    String obtenerApaterno = "";
    int idInsertar = 0;
    String claveDelVendedor;
    TableRowSorter trs = null;
    DefaultTableModel modelo;
    DefaultListModel listModel = new DefaultListModel();

    DefaultTableModel busqueda;
    DefaultListModel busqkuedaModelo = new DefaultListModel();

    public Vendedores() {
        initComponents();
        this.setLocationRelativeTo(null);
        PostgreSCrearBase();
        PostgreSSearchEmpleados();
        PostgreSListarEmpleados();
        PostgreSConsultarEmpleados();
        PostgreSConsultarID();
        PostgreSConsultarNombreDelID();

    }

    public Image getImage() {
        Image retvalue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("iconImages/Icon.png"));
        return retvalue;
    }

    public void PostgreSConexion() {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/trendy_dev", "trendyuser", "tr3ndyus3r");
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Error: \n" + ex.getMessage());
            System.exit(0);
        }
        System.out.println("¡Conexión Éxitosa!\n**************************************");
    }

    public void PostgreSCrearBase() {
        System.out.println((char) 27 + "[32mEspere: ");
        System.out.println((char) 27 + "[32m     Creando la base de datos....\n");
//        JOptionPane.showInternalInputDialog(rootPane, "Espere...\nCreando la base de datos.");
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "gpbs971635");
            stmt = c.createStatement();
            String crearBaseDeDatos = "CREATE DATABASE trendy_dev;";
            String cambiarPropietariodeTrendy_dev = "ALTER DATABASE trendy_dev OWNER TO trendyuser;";
            stmt.executeUpdate(crearBaseDeDatos);
            stmt.executeUpdate(cambiarPropietariodeTrendy_dev);
            System.out.println((char) 27 + "[32m         Se ha creado la base de datos <<trendy_dev>>' con éxito.\n");
            stmt.getResultSet();
            System.out.println((char) 27 + "[32m     Cambiando permisos de usuario.... OK!\n");
            JOptionPane.showMessageDialog(null, "Se ha creado la base de datos con éxito.", "!Creation Successfully¡", JOptionPane.INFORMATION_MESSAGE);
            PostgreSCrearTablas();
            System.out.println((char) 27 + "[32m!Listo!\n\n");
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println((char) 27 + "[31mError al crear la base de datos...");
            System.out.println((char) 27 + "[31m     Descripccion del " + ex.getMessage() + "\n");
        }
    }

    public void PostgreSCrearTablas() {
        System.out.println((char) 27 + "[32m     Creando la tabla Empleados...");
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/trendy_dev", "trendyuser", "tr3ndyus3r");
            stmt = c.createStatement();
            String sql1 = "CREATE TABLE empleado "
                    + "(ID NUMERIC PRIMARY KEY     NOT NULL, "
                    + " NOMBRE      TEXT, "
                    + " APATERNO    TEXT, "
                    + " AMATERNO    TEXT, "
                    + " CALLE       TEXT, "
                    + " COLONIA     TEXT, "
                    + " ESTADO      TEXT, "
                    + " CIUDAD      TEXT, "
                    + " CODIGOP     NUMERIC, "
                    + " EMAIL       TEXT, "
                    + " TELEFONO    NUMERIC, "
                    + " CLAVEVEN    TEXT    NOT NULL, "
                    + " PUESTO   INTEGER);";
            String usuarioAdministrador = "Postgres";
            stmt.executeUpdate(sql1);
            stmt.close();
            System.out.println((char) 27 + "[32m         ¡Las Tablas se crearón correctamente!\n");
        } catch (Exception ex) {
            System.out.println((char) 27 + "[31mOcurrio un error al Crear las tablas: " + ex.getMessage());
        }

    }

    public void PostgreSInsertarRegistros() {
        boolean[] caja = new boolean[12];
        boolean cajaLlenas = false;
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        caja[0] = jTextNombreVendedor.getText().isEmpty();
        caja[1] = jTextApaterno.getText().isEmpty();
        caja[2] = jTextAmaterno.getText().isEmpty();
        caja[3] = jTextCalle.getText().isEmpty();
        caja[4] = jTextColonia.getText().isEmpty();
        caja[5] = jComboEstado.getSelectedItem() == "----------";
        caja[6] = jTextPais.getText().isEmpty();
        caja[7] = jTextCodigoPostal.getText().isEmpty();
        caja[8] = jTextEmail.getText().isEmpty();
        caja[9] = jTextTelefono.getText().isEmpty();
        caja[10] = jTextClaveVendedor.getText().isEmpty();
        caja[11] = jComboPuesto.getSelectedItem() == "----------";
//        caja[16] = jDateFechaIngreso.getDate() == null;
        String email = jTextEmail.getText();
        Matcher mather = pattern.matcher(email);
        if (caja[0] || caja[1] || caja[2] || caja[3] || caja[4] || caja[5] || caja[6] || caja[7] || caja[8] || caja[9] || caja[10] || caja[11]) {
            cajaLlenas = true;
            JOptionPane.showMessageDialog(null, "Todos Los Campos son Necesario: ", "Importante", JOptionPane.INFORMATION_MESSAGE);
        } else if (mather.find() == false) {
            cajaLlenas = true;
            JOptionPane.showMessageDialog(rootPane, "El email es invalido..\n Ejemplo: example@example.ejp");
        } else {
            cajaLlenas = false;
            NombreVendedor = jTextNombreVendedor.getText();
            Apaterno = jTextApaterno.getText();
            Amaterno = jTextAmaterno.getText();
            Calle = jTextCalle.getText();
            Colonia = jTextColonia.getText();
            Estado = jComboEstado.getSelectedItem().toString();
            Pais = jTextPais.getText();
            CodigoPostal = jTextCodigoPostal.getText();
            Email = jTextEmail.getText();
            Telefono = jTextTelefono.getText();
            ClaveVendedor = jTextClaveVendedor.getText();
            PuestoVendedor_id = 0;
//            Fecha1 = jDateFechaIngreso.getDate();
//            SimpleDateFormat Fechaa1 = new SimpleDateFormat("dd/MM/yyyy");
//            FechaIngreso = Fechaa1.format(Fecha1);
//            FechaBaja = "00/00/0000";
            if (null != jComboPuesto.getSelectedItem().toString()) {
                switch (jComboPuesto.getSelectedItem().toString()) {
                    case "Vendedora":
                        PuestoVendedor_id = 1;
                        break;
                    case "Gerente":
                        PuestoVendedor_id = 2;
                        break;
                    case "Subgerente":
                        PuestoVendedor_id = 3;
                        break;
                    case "Visual":
                        PuestoVendedor_id = 4;
                        break;
                    case "Maquillista Jr":
                        PuestoVendedor_id = 5;
                        break;
                    case "Senior Visual":
                        PuestoVendedor_id = 6;
                        break;
                    default:
                        break;
                }
            }
            try {
//                PostgreSConsultarID();
                ClaveVendedor = jTextClaveVendedor.getText();
                try {
                    PostgreSConexion();
                    stmt = c.createStatement();
                    rs = stmt.executeQuery("SELECT id FROM empleado WHERE  claveven='" + ClaveVendedor + "';");
                    while (rs.next()) {
                        idInsertar = rs.getInt("id");
                        System.err.println("ID = " + idInsertar);
                        System.out.println("La clave: " + ClaveVendedor + " del Vendedor ya esta ocupado por otro vendedor: " + idInsertar);
                        PostgreSConsultarNombreDelID();
                        JOptionPane.showMessageDialog(null, "La Calve: " + ClaveVendedor + " ya se esta Usando por: \n" + obtenerNombre + " " + obtenerApaterno + ".", "Clave Duplicada", JOptionPane.ERROR_MESSAGE);
                        rs.close();
                        c.close();
                        break;
                    }
                } catch (SQLException ex) {
                }
                stmt = c.createStatement();
                String sql = "INSERT INTO empleado(id,nombre,apaterno,amaterno,calle,colonia,estado,ciudad,codigop,email,telefono,claveven,puesto) "
                        + "VALUES (" + identificador + ", '" + NombreVendedor + "', '" + Apaterno + "', '" + Amaterno + "', '" + Calle + "', '" + Colonia + "', '" + Estado
                        + "', '" + Pais + "', '" + CodigoPostal + "', '" + Email + "', '" + Telefono + "', '" + ClaveVendedor + "', " + PuestoVendedor_id + ");";
                stmt.executeUpdate(sql);
                System.err.println("Los Registros se han guardado con éxito\n");
                JOptionPane.showMessageDialog(null, "El registro de: " + NombreVendedor + " " + Apaterno + "\nSe ha guarado con éxito", "Guardado", JOptionPane.INFORMATION_MESSAGE);
                limpiarForm();
                PostgreSConsultarID();
                PostgreSConsultarEmpleados();
            } catch (SQLException ex) {
                System.err.println("Error en: " + ex);

            }
        }
    }

    public void PostgreSConsultarID() {
        int id;
        int autoIncrement;
        try {
            PostgreSConexion();
            stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT ID FROM empleado ORDER BY id DESC LIMIT 1;");
            while (rs.next()) {
                id = rs.getInt("ID");
                for (autoIncrement = 1; autoIncrement <= id; autoIncrement++) {
                    contarID = autoIncrement;
                }
            }
            rs.close();
            c.close();
        } catch (SQLException e) {
            System.out.println((char) 27 + "[31mError en: \n");
            System.out.println((char) 27 + "[31m" + e.getClass().getName() + ": " + e.getMessage());
        }
        identificador = contarID + 1;
        System.out.println("¡Consulta correcta!");
        System.out.println("Siguiente ID: " + identificador);
    }

    public void PostgreSConsultarNombreDelID() {
        try {
            PostgreSConexion();
            stmt = c.createStatement();
            int Nombre = contarID - 1;
            queryVendedor = stmt.executeQuery("SELECT * FROM empleado WHERE id= '" + idInsertar + "';");
            while (queryVendedor.next()) {
                obtenerClaveVendedor = queryVendedor.getInt("claveven");
                obtenerNombre = queryVendedor.getString("nombre");
                obtenerApaterno = queryVendedor.getString("apaterno");
            }
            System.out.println("id buscado: " + Nombre);
            System.out.println("id: " + obtenerClaveVendedor);
            System.out.println("Nombre: " + obtenerNombre);
            System.out.println("Apaterno: " + obtenerApaterno);

        } catch (SQLException ex) {
        }
    }

    public void limpiarForm() {
        jTextNombreVendedor.setText("");
        jTextApaterno.setText("");
        jTextAmaterno.setText("");
        jTextCalle.setText("");
//        jTextExterior.setText("");
//        jTextInterior.setText("");
        jTextColonia.setText("");
//        jTextMunicipio.setText("");
        jComboEstado.setSelectedItem(0);
        jTextPais.setText("");
        jTextCodigoPostal.setText("");
        jTextEmail.setText("");
        jTextTelefono.setText("");
//        jTextCelular.setText("");
        jTextClaveVendedor.setText("");
        jComboPuesto.setSelectedItem(0);
//        jDateFechaIngreso.setDate(null);
        PostgreSConsultarID();
    }

    public void PostgreSConsultarEmpleados() {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/trendy_dev", "trendyuser", "tr3ndyus3r");
            stmt = c.createStatement();
            queryEmpleados = stmt.executeQuery("SELECT nombre,apaterno,telefono,claveven,puesto FROM empleado");
            rsm = queryEmpleados.getMetaData();
            ArrayList<Object[]> data = new ArrayList<>();
            while (queryEmpleados.next()) {
                Object[] rows = new Object[rsm.getColumnCount()];
                for (int i = 0; i < rows.length; i++) {
                    rows[i] = queryEmpleados.getObject(i + 1);
                }
                data.add(rows);
            }
            modelo = (DefaultTableModel) this.jTableListarRegistros.getModel();
            for (int i = 0; i < data.size(); i++) {
                modelo.addRow(data.get(i));
            }
            stmt.close();
            c.close();
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }

    public void PostgreSUpdateEmpleados() {
        boolean[] caja = new boolean[9];
        boolean cajaLlenas = false;
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        caja[0] = jTextModificarNombre.getText().isEmpty();
        caja[1] = jTextModificarApaterno.getText().isEmpty();
        caja[2] = jTextModificarAmaterno.getText().isEmpty();
        caja[3] = jTextModificarCalle.getText().isEmpty();
        caja[4] = jTextModificarColonia.getText().isEmpty();
        caja[5] = jComboModificarEstado.getSelectedItem() == "----------";
        caja[6] = jTextModificarCiudad.getText().isEmpty();
        caja[7] = jTextModificarCodigoPostal.getText().isEmpty();
        caja[8] = jTextModificarEmail.getText().isEmpty();
        String email = jTextEmail.getText();
        Matcher mather = pattern.matcher(email);
        if (caja[0] || caja[1] || caja[2] || caja[3] || caja[4] || caja[5] || caja[6] || caja[7] || caja[8] || caja[9] || caja[10] || caja[11]) {
            cajaLlenas = true;
            JOptionPane.showMessageDialog(null, "Debe indicar la clave del vendedor: ", "Importante", JOptionPane.INFORMATION_MESSAGE);
        } else if (mather.find() == false) {
            cajaLlenas = true;
            JOptionPane.showMessageDialog(rootPane, "El email es invalido..\n Ejemplo: example@example.ejp");
        } else {
            cajaLlenas = false;
            NombreVendedor = jTextModificarNombre.getText();
            Apaterno = jTextModificarApaterno.getText();
            Amaterno = jTextModificarAmaterno.getText();
            Calle = jTextModificarCalle.getText();
            Colonia = jTextModificarColonia.getText();
            Estado = jComboModificarEstado.getSelectedItem().toString();
            Pais = jTextModificarCiudad.getText();
            CodigoPostal = jTextModificarCodigoPostal.getText();
            Email = jTextModificarEmail.getText();
            PuestoVendedor_id = 0;
            System.out.println("Clave del vendedor a modificar: " + claveDelVendedor);
            try {
                Class.forName("org.postgres.Driver");
                c = DriverManager.getConnection("jdbc::postgresql://localhost:5432/trendy_dev", "trendyuser", "tr3ndyus3r");
                c.setAutoCommit(false);
                stmt = c.createStatement();
                String sqlUpdate = "UPDATE empleado SET nombre=" + NombreVendedor +", apaterno";

            } catch (ClassNotFoundException | SQLException ex) {
            }
        }
    }

    

    public void PostgreSSearchEmpleados() {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/trendy_dev", "trendyuser", "tr3ndyus3r");
            stmt = c.createStatement();
            queryBuscaryModificarEmpleado = stmt.executeQuery("SELECT nombre,apaterno,amaterno,calle,colonia,ciudad,estado,email,codigop,claveven FROM empleado");
            rsm = queryBuscaryModificarEmpleado.getMetaData();
            ArrayList<Object[]> busarDatos = new ArrayList<>();
            while (queryBuscaryModificarEmpleado.next()) {
                Object[] rows = new Object[rsm.getColumnCount()];
                for (int i = 0; i < rows.length; i++) {
                    rows[i] = queryBuscaryModificarEmpleado.getObject(i + 1);
                }
                busarDatos.add(rows);
            }
            modelo = (DefaultTableModel) this.jTableModificarRegsitros1.getModel();
            for (int i = 0; i < busarDatos.size(); i++) {
                modelo.addRow(busarDatos.get(i));
            }
            stmt.close();
            c.close();
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }

    }

    public void PostgreSListarEmpleados() {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/trendy_dev", "trendyuser", "tr3ndyus3r");
            stmt = c.createStatement();
            queryEmpleados = stmt.executeQuery("SELECT id,nombre,apaterno,amaterno,puesto,claveven,email FROM empleado");
            rsm = queryEmpleados.getMetaData();
            ArrayList<Object[]> data = new ArrayList<>();
            while (queryEmpleados.next()) {
                Object[] rows = new Object[rsm.getColumnCount()];
                for (int i = 0; i < rows.length; i++) {
                    rows[i] = queryEmpleados.getObject(i + 1);
                }
                data.add(rows);
            }
            modelo = (DefaultTableModel) this.jTableConsultarRegistros.getModel();
            for (int i = 0; i < data.size(); i++) {
                modelo.addRow(data.get(i));
            }
            stmt.close();
            c.close();
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jScrollPane2 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jTableModificarRegistros = new javax.swing.JTabbedPane();
        jPanelRegistro = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextNombreVendedor = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextApaterno = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextAmaterno = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextCalle = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextColonia = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jComboEstado = new javax.swing.JComboBox<>();
        jTextPais = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextCodigoPostal = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextEmail = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextTelefono = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel16 = new javax.swing.JLabel();
        jTextClaveVendedor = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jComboPuesto = new javax.swing.JComboBox<>();
        jButtonRegistrarEmpleado = new javax.swing.JButton();
        jButtonNuevoEmpleado = new javax.swing.JButton();
        jButtonLimpiarYCerrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableListarRegistros = new javax.swing.JTable();
        jPanelModificar = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jTextBuscarYModificarEmpleado = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTableModificarRegsitros1 = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        jTextModificarNombre = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jTextModificarApaterno = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jTextModificarAmaterno = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jTextModificarCalle = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jTextModificarColonia = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jTextModificarCiudad = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jComboModificarEstado = new javax.swing.JComboBox<>();
        jLabel30 = new javax.swing.JLabel();
        jTextModificarEmail = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jTextModificarCodigoPostal = new javax.swing.JTextField();
        jButtonEditarVendedor = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jPanelConsultar = new javax.swing.JPanel();
        jTextConsultarVendedorpor = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTableConsultarRegistros = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButtonSalirdelSistema = new javax.swing.JButton();

        jToolBar1.setRollover(true);

        jScrollPane2.setViewportView(jEditorPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Trendy - Vendedores");
        setIconImage(getImage());
        setResizable(false);

        jLabel2.setText("Nombre:");

        jLabel3.setText("Apellido Paterno:");

        jLabel4.setText("Apellido Materno:");

        jLabel5.setText("Direccion:");

        jLabel8.setText("Colonia:");

        jLabel10.setText("Estado:");

        jLabel11.setText("Ciudad:");

        jComboEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "----------", "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Chiapas", "Chihuahua", "Ciudad de México", "Coahuila de Zaragoza", "Colima", "Durango", "México", "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Michoacán", "Morelos", "Nayarit", "Nuevo León", "Oaxaca", "Puebla", "Querétaro", "Quintana Roo", "San Luis Potosí", "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatán", "Zacatecas" }));

        jLabel12.setText("CP:");

        jTextCodigoPostal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextCodigoPostalKeyTyped(evt);
            }
        });

        jLabel13.setText("Email:");

        jTextEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextEmailKeyTyped(evt);
            }
        });

        jLabel14.setText("Telefono:");

        jTextTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextTelefonoKeyTyped(evt);
            }
        });

        jLabel16.setText("Clave de Vendedor:");

        jTextClaveVendedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextClaveVendedorKeyTyped(evt);
            }
        });

        jLabel17.setText("Puesto del Vendedor:");

        jComboPuesto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "----------", "Vendedora", "Gerente", "Subgerente", "Visual", "Maquillista Jr", "Senior Visual" }));
        jComboPuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboPuestoActionPerformed(evt);
            }
        });

        jButtonRegistrarEmpleado.setText("Registrar");
        jButtonRegistrarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegistrarEmpleadoActionPerformed(evt);
            }
        });

        jButtonNuevoEmpleado.setText(" Nuevo Registro");
        jButtonNuevoEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNuevoEmpleadoActionPerformed(evt);
            }
        });

        jButtonLimpiarYCerrar.setText("Cancelar");

        jTableListarRegistros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Apellido Paterno", "Telefono", "Clave Vendedor", "Puesto"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableListarRegistros);
        if (jTableListarRegistros.getColumnModel().getColumnCount() > 0) {
            jTableListarRegistros.getColumnModel().getColumn(0).setResizable(false);
        }

        javax.swing.GroupLayout jPanelRegistroLayout = new javax.swing.GroupLayout(jPanelRegistro);
        jPanelRegistro.setLayout(jPanelRegistroLayout);
        jPanelRegistroLayout.setHorizontalGroup(
            jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRegistroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelRegistroLayout.createSequentialGroup()
                        .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17))
                        .addGap(73, 73, 73)
                        .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelRegistroLayout.createSequentialGroup()
                                .addComponent(jTextClaveVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jComboPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButtonNuevoEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonRegistrarEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonLimpiarYCerrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(29, 29, 29))
                    .addGroup(jPanelRegistroLayout.createSequentialGroup()
                        .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelRegistroLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextNombreVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(jTextApaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jTextAmaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1))
                        .addContainerGap())
                    .addGroup(jPanelRegistroLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextCalle, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextColonia, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanelRegistroLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(jComboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextPais, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextCodigoPostal, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator1)))
        );
        jPanelRegistroLayout.setVerticalGroup(
            jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelRegistroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextNombreVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTextApaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextAmaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextCalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(jTextTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jTextColonia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jTextPais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jTextCodigoPostal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jTextEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextClaveVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonRegistrarEmpleado))
                .addGap(5, 5, 5)
                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jButtonNuevoEmpleado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonLimpiarYCerrar)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        jTableModificarRegistros.addTab("Registro de Empleados", jPanelRegistro);

        jLabel20.setText("Modificar Vendedor por Clave:");

        jTextBuscarYModificarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextBuscarYModificarEmpleadoActionPerformed(evt);
            }
        });
        jTextBuscarYModificarEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextBuscarYModificarEmpleadoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextBuscarYModificarEmpleadoKeyTyped(evt);
            }
        });

        jTableModificarRegsitros1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NOMBRE", "AP. PATERNO", "AP. MATERNO", "CALLE", "COLONIA", "CIUDAD", "ESTADO", "EMAIL", "CP", "CLAVE EMPLEADO"
            }
        ));
        jTableModificarRegsitros1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableModificarRegsitros1MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTableModificarRegsitros1);

        jLabel21.setText("Nombre:");

        jTextModificarNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextModificarNombreActionPerformed(evt);
            }
        });

        jLabel22.setText("Apellido Paterno:");

        jLabel23.setText("Apellido Materno:");

        jLabel24.setText("Calle:");

        jLabel27.setText("Colonia:");

        jLabel28.setText("Ciudad:");

        jLabel29.setText("Estado:");

        jComboModificarEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "----------", "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Chiapas", "Chihuahua", "Ciudad de México", "Coahuila de Zaragoza", "Colima", "Durango", "México", "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Michoacán", "Morelos", "Nayarit", "Nuevo León", "Oaxaca", "Puebla", "Querétaro", "Quintana Roo", "San Luis Potosí", "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatán", "Zacatecas" }));

        jLabel30.setText("Email:");

        jLabel31.setText("CP:");

        jButtonEditarVendedor.setText("Editar Vendedor");
        jButtonEditarVendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditarVendedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelModificarLayout = new javax.swing.GroupLayout(jPanelModificar);
        jPanelModificar.setLayout(jPanelModificarLayout);
        jPanelModificarLayout.setHorizontalGroup(
            jPanelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelModificarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanelModificarLayout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(18, 18, 18)
                        .addComponent(jTextBuscarYModificarEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelModificarLayout.createSequentialGroup()
                        .addGap(167, 167, 167)
                        .addComponent(jSeparator2))
                    .addGroup(jPanelModificarLayout.createSequentialGroup()
                        .addGroup(jPanelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelModificarLayout.createSequentialGroup()
                                .addGroup(jPanelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel24))
                                .addGap(18, 18, 18)
                                .addGroup(jPanelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelModificarLayout.createSequentialGroup()
                                        .addGroup(jPanelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanelModificarLayout.createSequentialGroup()
                                                .addComponent(jLabel29)
                                                .addGap(3, 3, 3)
                                                .addComponent(jComboModificarEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jTextModificarCalle, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel27)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextModificarColonia, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanelModificarLayout.createSequentialGroup()
                                        .addComponent(jTextModificarNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel22)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextModificarApaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel23)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextModificarAmaterno, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanelModificarLayout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addGap(18, 18, 18)
                                .addComponent(jTextModificarCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextModificarEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel31)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextModificarCodigoPostal, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(85, 85, 85)))
                        .addGap(55, 55, 55))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelModificarLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonEditarVendedor)))
                .addContainerGap())
        );
        jPanelModificarLayout.setVerticalGroup(
            jPanelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelModificarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jTextModificarNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(jTextModificarApaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(jTextModificarAmaterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jTextModificarCalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(jTextModificarColonia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(jComboModificarEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30)
                    .addComponent(jTextModificarEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31)
                    .addComponent(jTextModificarCodigoPostal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextModificarCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(jButtonEditarVendedor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelModificarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextBuscarYModificarEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        jTableModificarRegistros.addTab("Modificar Empleados", jPanelModificar);

        jTextConsultarVendedorpor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextConsultarVendedorporKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextConsultarVendedorporKeyTyped(evt);
            }
        });

        jLabel19.setText("Buscar Vendedor:");

        jTableConsultarRegistros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NOMBRE", "AP. PATERNO", "AP. MATERNO", "PUESTO", "CLAVE", "CORREO"
            }
        ));
        jScrollPane5.setViewportView(jTableConsultarRegistros);

        javax.swing.GroupLayout jPanelConsultarLayout = new javax.swing.GroupLayout(jPanelConsultar);
        jPanelConsultar.setLayout(jPanelConsultarLayout);
        jPanelConsultarLayout.setHorizontalGroup(
            jPanelConsultarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConsultarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelConsultarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 975, Short.MAX_VALUE)
                    .addGroup(jPanelConsultarLayout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(44, 44, 44)
                        .addComponent(jTextConsultarVendedorpor, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanelConsultarLayout.setVerticalGroup(
            jPanelConsultarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConsultarLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanelConsultarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextConsultarVendedorpor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        jTableModificarRegistros.addTab("Consultar Empleados", jPanelConsultar);

        jLabel1.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        jLabel1.setText("Trendy Vendedores");

        jButtonSalirdelSistema.setText("Salir");
        jButtonSalirdelSistema.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalirdelSistemaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTableModificarRegistros)
            .addGroup(layout.createSequentialGroup()
                .addGap(223, 223, 223)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonSalirdelSistema)
                .addGap(55, 55, 55))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButtonSalirdelSistema))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTableModificarRegistros, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboPuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboPuestoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboPuestoActionPerformed

    private void jButtonRegistrarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegistrarEmpleadoActionPerformed
        PostgreSInsertarRegistros();
    }//GEN-LAST:event_jButtonRegistrarEmpleadoActionPerformed

    private void jTextModificarNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextModificarNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextModificarNombreActionPerformed

    private void jTextBuscarYModificarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextBuscarYModificarEmpleadoActionPerformed

    }//GEN-LAST:event_jTextBuscarYModificarEmpleadoActionPerformed

    private void jTextBuscarYModificarEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextBuscarYModificarEmpleadoKeyTyped
        jTextBuscarYModificarEmpleado.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                trs.setRowFilter(RowFilter.regexFilter("(?i)" + jTextBuscarYModificarEmpleado.getText(), 0, 1, 2, 3, 4));
            }
        });
        trs = new TableRowSorter(modelo);
        jTableModificarRegsitros1.setRowSorter(trs);
    }//GEN-LAST:event_jTextBuscarYModificarEmpleadoKeyTyped

    private void jButtonNuevoEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoEmpleadoActionPerformed
        limpiarForm();
    }//GEN-LAST:event_jButtonNuevoEmpleadoActionPerformed

    private void jButtonSalirdelSistemaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirdelSistemaActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButtonSalirdelSistemaActionPerformed

    private void jTextBuscarYModificarEmpleadoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextBuscarYModificarEmpleadoKeyReleased

    }//GEN-LAST:event_jTextBuscarYModificarEmpleadoKeyReleased

    private void jButtonEditarVendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditarVendedorActionPerformed
        PostgreSUpdateEmpleados();
    }//GEN-LAST:event_jButtonEditarVendedorActionPerformed

    private void jTextConsultarVendedorporKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextConsultarVendedorporKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextConsultarVendedorporKeyReleased

    private void jTextConsultarVendedorporKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextConsultarVendedorporKeyTyped
        jTextConsultarVendedorpor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent ke) {
                trs.setRowFilter(RowFilter.regexFilter("(?i)" + jTextConsultarVendedorpor.getText(), 0, 1, 2, 3, 4));
            }
        });
        trs = new TableRowSorter(modelo);
        jTableConsultarRegistros.setRowSorter(trs);
    }//GEN-LAST:event_jTextConsultarVendedorporKeyTyped

    private void jTextTelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextTelefonoKeyTyped
        char validar = evt.getKeyChar();
        if (((validar < '0') || (validar > '9') && (validar != KeyEvent.VK_BACK_SPACE))) {
            getToolkit().beep();
            evt.consume();
        }

    }//GEN-LAST:event_jTextTelefonoKeyTyped

    private void jTextCodigoPostalKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextCodigoPostalKeyTyped
        char validar = evt.getKeyChar();
        if (((validar < '0') || (validar > '9') && (validar != KeyEvent.VK_BACK_SPACE))) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_jTextCodigoPostalKeyTyped

    private void jTextClaveVendedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextClaveVendedorKeyTyped
        char validar = evt.getKeyChar();
        if (((validar < '0') || (validar > '9') && (validar != KeyEvent.VK_BACK_SPACE))) {
            getToolkit().beep();
            evt.consume();
        }
    }//GEN-LAST:event_jTextClaveVendedorKeyTyped

    private void jTextEmailKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextEmailKeyTyped

    }//GEN-LAST:event_jTextEmailKeyTyped

    private void jTableModificarRegsitros1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableModificarRegsitros1MouseClicked
        int recoger = this.jTableModificarRegsitros1.getSelectedRow();
        this.jTextModificarNombre.setText(jTableModificarRegsitros1.getValueAt(recoger, 0).toString());
        this.jTextModificarApaterno.setText(jTableModificarRegsitros1.getValueAt(recoger, 1).toString());
        this.jTextModificarAmaterno.setText(jTableModificarRegsitros1.getValueAt(recoger, 2).toString());
        this.jTextModificarCalle.setText(jTableModificarRegsitros1.getValueAt(recoger, 3).toString());
        this.jTextModificarColonia.setText(jTableModificarRegsitros1.getValueAt(recoger, 4).toString());
        this.jTextModificarCiudad.setText(jTableModificarRegsitros1.getValueAt(recoger, 5).toString());
        //this.jComboModificarEstado.set(jTableModificarRegsitros1.getValueAt(recoger, 6).toString());
        this.jTextModificarEmail.setText(jTableModificarRegsitros1.getValueAt(recoger, 7).toString());
        this.jTextModificarCodigoPostal.setText(jTableModificarRegsitros1.getValueAt(recoger, 8).toString());
        claveDelVendedor = (jTableModificarRegsitros1.getValueAt(recoger, 9).toString());
    }//GEN-LAST:event_jTableModificarRegsitros1MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Vendedores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Vendedores().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonEditarVendedor;
    private javax.swing.JButton jButtonLimpiarYCerrar;
    private javax.swing.JButton jButtonNuevoEmpleado;
    private javax.swing.JButton jButtonRegistrarEmpleado;
    private javax.swing.JButton jButtonSalirdelSistema;
    private javax.swing.JComboBox<String> jComboEstado;
    private javax.swing.JComboBox<String> jComboModificarEstado;
    private javax.swing.JComboBox<String> jComboPuesto;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanelConsultar;
    private javax.swing.JPanel jPanelModificar;
    private javax.swing.JPanel jPanelRegistro;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTableConsultarRegistros;
    private javax.swing.JTable jTableListarRegistros;
    private javax.swing.JTabbedPane jTableModificarRegistros;
    private javax.swing.JTable jTableModificarRegsitros1;
    private javax.swing.JTextField jTextAmaterno;
    private javax.swing.JTextField jTextApaterno;
    private javax.swing.JTextField jTextBuscarYModificarEmpleado;
    private javax.swing.JTextField jTextCalle;
    private javax.swing.JTextField jTextClaveVendedor;
    private javax.swing.JTextField jTextCodigoPostal;
    private javax.swing.JTextField jTextColonia;
    private javax.swing.JTextField jTextConsultarVendedorpor;
    private javax.swing.JTextField jTextEmail;
    private javax.swing.JTextField jTextModificarAmaterno;
    private javax.swing.JTextField jTextModificarApaterno;
    private javax.swing.JTextField jTextModificarCalle;
    private javax.swing.JTextField jTextModificarCiudad;
    private javax.swing.JTextField jTextModificarCodigoPostal;
    private javax.swing.JTextField jTextModificarColonia;
    private javax.swing.JTextField jTextModificarEmail;
    private javax.swing.JTextField jTextModificarNombre;
    private javax.swing.JTextField jTextNombreVendedor;
    private javax.swing.JTextField jTextPais;
    private javax.swing.JTextField jTextTelefono;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
