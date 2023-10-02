package br.com.alura.bytebank.domain.conta;

import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ContaDAO {


    private Connection conn;


    ContaDAO(Connection connection) {
        this.conn = connection;
    }

    public void salvar(DadosAberturaConta dadosDaConta) {

        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), cliente, BigDecimal.ZERO);

        String sql = "INSERT INTO clientes.conta (numero, saldo, cliente_nome, cliente_cpf, cliente_email)" +
                "VALUES (?, ?, ?, ?, ?)";

        try {


            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, conta.getNumero());
            ps.setBigDecimal(2, BigDecimal.ZERO);
            ps.setString(3, dadosDaConta.dadosCliente().nome());
            ps.setString(4, dadosDaConta.dadosCliente().cpf());
            ps.setString(5, dadosDaConta.dadosCliente().email());

            ps.execute();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Conta> listar() {
        Set<Conta> contas = new HashSet<>();

        String sql = "SELECT * FROM clientes.conta";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Integer numero = rs.getInt(1);
                BigDecimal saldo = rs.getBigDecimal(2);
                String nome = rs.getString(3);
                String cpf = rs.getString(4);
                String email = rs.getString(5);

                DadosCadastroCliente dadosCadastroCliente =
                        new DadosCadastroCliente(nome, cpf, email);

                Cliente cliente = new Cliente(dadosCadastroCliente);

                contas.add(new Conta(numero, cliente, saldo));


            }

            ps.close();
            rs.close();
            conn.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return contas;
    }


    public Conta buscarNumero(Integer numero) {

         Conta conta = null;

            String sql = "SELECT * FROM clientes.conta WHERE numero = ?";

            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, numero);
                ResultSet rs = ps.executeQuery();

                while(rs.next()) {
                    Integer num = rs.getInt(1);
                    BigDecimal saldo = rs.getBigDecimal(2);
                    String nome = rs.getString(3);
                    String cpf = rs.getString(4);
                    String email = rs.getString(5);

                    DadosCadastroCliente dadosCadastroCliente = new DadosCadastroCliente(nome, cpf, email);
                    Cliente cliente = new Cliente(dadosCadastroCliente);
                    conta = (new Conta(num, cliente, saldo));

                }


            ps.close();
            rs.close();
            conn.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return conta;
        }



        public void alterar(Integer numeroDaConta, BigDecimal valor){

        String sql = "UPDATE clientes.conta SET saldo = ? WHERE numero= ?";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setBigDecimal(1, valor);
            ps.setInt(2, numeroDaConta);


            ps.execute();
            ps.close();
            conn.close();


        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }

        }




    public void remove(Integer numeroConta){
               String sql= "DELETE FROM clientes.conta WHERE numero = ?";
               PreparedStatement ps;

               try {
                     ps = conn.prepareStatement(sql);
                     ps.setInt(1,numeroConta);


                     ps.execute();
                     conn.close();

               }catch(SQLException e){
                   throw new RuntimeException(e);
        }
    }




}








