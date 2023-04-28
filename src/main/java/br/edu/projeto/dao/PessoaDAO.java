package br.edu.projeto.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.sql.DataSource;

import br.edu.projeto.model.TipoPermissao;
import br.edu.projeto.model.Pessoa;
import br.edu.projeto.util.DbUtil;
import br.edu.projeto.util.Permissao;

//Classe DAO responsável pelas regras de negócio envolvendo operações de persistência de dados
//Indica-se a acriação de um DAO específico para cada Modelo

//Anotação EJB que indica que Bean (objeto criado para a classe) será comum para toda a aplicação
//Isso faz com que recursos computacionais sejam otimizados e garante maior segurança nas transações com o banco
@Stateful
public class PessoaDAO implements Serializable{
	private static final long serialVersionUID = 1L;

	@Inject
	private DataSource ds;
	
    public Pessoa findByCpf(String cpf) {
    	Pessoa u = new Pessoa();
    	u.setCpf(cpf);
    	Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try {
			con = this.ds.getConnection();
			ps = con.prepareStatement("SELECT nome, email FROM pessoa WHERE cpf = ?");
			ps.setString(1, cpf);
			rs = ps.executeQuery();
			if (rs.next()) {
				u.setEmail(rs.getString("email"));
				u.setNome(rs.getString("nome"));
			}
		} catch (SQLException e) {e.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
        return u;
    }
    
    public List<Pessoa> listAll() {
    	List<Pessoa> pessoas = new ArrayList<Pessoa>();
    	Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try {
			con = this.ds.getConnection();
			ps = con.prepareStatement("SELECT nome, nome_social, genero, email  FROM pessoa");
			rs = ps.executeQuery();
			while (rs.next()) {
				Pessoa u = new Pessoa();
				u.setNome(rs.getString("nome"));
				u.setNome(rs.getString("nome_social"));
				u.setNome(rs.getString("genero"));
				u.setNome(rs.getString("email"));
				pessoas.add(u);
			}
			con.close();
		} catch (SQLException e) {e.printStackTrace();
		} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
        return pessoas;
    }
    
    public Boolean insert(Pessoa u) {
    	Boolean resultado = false;
    	Connection con = null;
    	PreparedStatement ps = null;
    	PreparedStatement ps2 = null;
    	try {
	    	con = this.ds.getConnection();
	    	con.setAutoCommit(false);
	    	try {				
				ps = con.prepareStatement("INSERT INTO pessoa (cpf, nome, nome_social, altura, massa, genero, idade, email, telefone, endereco) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				ps.setString(1, u.getCpf());
				ps.setString(2, u.getNome());
				ps.setString(3, u.getNomeSocial());
				ps.setDouble(4, u.getAltura());
				ps.setDouble(5, u.getMassa());
				ps.setString(6, u.getGenero());
				ps.setInt(7, u.getIdade());
				ps.setString(8, u.getEmail());
				ps.setString(9, u.getTelefone());
				ps.setString(10, u.getEndereco());
				ps.execute();
				con.commit();
				resultado = true;
			} catch (SQLException e) {
				e.printStackTrace();
				con.rollback();
			}
    	} catch (SQLException e) {e.printStackTrace();
    	} finally {
			DbUtil.closePreparedStatement(ps);
			DbUtil.closePreparedStatement(ps2);
			DbUtil.closeConnection(con);
		}
    	return resultado;
    }
    
    public Boolean update(Pessoa u) {
    	Boolean resultado = false;
    	Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try {
	    	con = this.ds.getConnection();
	    	con.setAutoCommit(false);
	    	try {				
				ps = con.prepareStatement("UPDATE pessoa SET nome_social = ?,  altura = ?, massa = ?, genero = ?, idade = ?, email = ?, telfone = ?, endereco = ? WHERE cpf	 = ?");
				ps.setString(1, u.getNomeSocial());
				ps.setDouble(2, u.getAltura());
				ps.setDouble(3, u.getMassa());
				ps.setString(4, u.getGenero());
				ps.setInt(5, u.getIdade());
				ps.setString(6, u.getEmail());
				ps.setString(7, u.getTelefone());
				ps.setString(8, u.getEndereco());
				ps.setString(9, u.getCpf());
				ps.execute();	
				con.commit();
				resultado = true;
			} catch (SQLException e) {
				e.printStackTrace();
				con.rollback();
			}
    	} catch (SQLException e) {e.printStackTrace();
    	} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
    	return resultado;
    }
    
    public Boolean delete(Pessoa u) {
    	Boolean resultado = false;
    	Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try {
	    	con = this.ds.getConnection();
	    	con.setAutoCommit(false);
	    	try {				
				ps = con.prepareStatement("DELETE FROM pessoa WHERE cpf = ?");
				ps.setString(1, u.getCpf());
				ps.execute();
				con.commit();
				resultado = true;
			} catch (SQLException e) {
				e.printStackTrace();
				con.rollback();
			}
    	} catch (SQLException e) {e.printStackTrace();
    	} finally {
			DbUtil.closeResultSet(rs);
			DbUtil.closePreparedStatement(ps);
			DbUtil.closeConnection(con);
		}
    	return resultado;
    }
}
