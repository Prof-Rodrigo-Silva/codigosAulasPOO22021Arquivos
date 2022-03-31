package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import classes.modelo.Aluno;
import classes.modelo.BeanAlunoFone;
import classes.modelo.Telefone;
import conexaojdbc.SingleConnection;

public class ClasseDAO {
	
	private Connection connection;

	public ClasseDAO() {
		connection = SingleConnection.getConnetion();
	}

	public void salvar(Aluno aluno) {
		try {
			//String sql = "INSERT INTO alunojdbc(id, nome, email) VALUES (?,?,?)";
			String sql = "INSERT INTO alunojdbc(nome, email) VALUES (?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			/*statement.setLong(1, 1);
			 statement.setString(2, "Rodrigo");
			 statement.setString(3,"rodrigoteste@gmail.com");

		    statement.setLong(1, aluno.getId());
			statement.setString(2, aluno.getNome());
			statement.setString(3, aluno.getEmail());*/

			statement.setString(1, aluno.getNome());	
			statement.setString(2, aluno.getEmail());

			statement.execute();
			connection.commit();// Executa no banco

		} catch (Exception e) {
			try {
				connection.rollback();// Reverte a operação
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public List<Aluno> listar() throws Exception {

		List<Aluno> list = new ArrayList<Aluno>();

		String sql = "SELECT * FROM alunojdbc";

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			Aluno aluno = new Aluno();
			aluno.setId(resultado.getLong("id"));
			aluno.setNome(resultado.getString("nome"));
			aluno.setEmail(resultado.getString("email"));

			list.add(aluno);

		}

		return list;
	}

	public Aluno buscar(Long id) throws Exception {
		Aluno aluno = new Aluno();

		String sql = "SELECT * FROM alunojdbc WHERE id =" + id;

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			aluno.setId(resultado.getLong("id"));
			aluno.setNome(resultado.getString("nome"));
			aluno.setEmail(resultado.getString("email"));

		}

		return aluno;
	}

	public void atualizar(Aluno aluno) {

		try {
			String sql = "UPDATE alunojdbc SET nome = ? WHERE id = " + aluno.getId();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, aluno.getNome());
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public void deletar(Long id) {
		try {
			String sql = "DELETE FROM alunojdbc WHERE id = "+id;
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.execute();
			connection.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}	
	}
	
	public void salvarTelefone(Telefone telefone) {
		try {
			String sql = "INSERT INTO telefonealuno( numero, tipo, usuario) VALUES (?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, telefone.getNumero());	
			statement.setString(2, telefone.getTipo());
			statement.setLong(3, telefone.getIdAluno());

			statement.execute();
			connection.commit();// Executa no banco

		} catch (Exception e) {
			try {
				connection.rollback();// Reverte a operação
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	public List<BeanAlunoFone> listarAlunoFone(Long idAluno) throws Exception {

		List<BeanAlunoFone> list = new ArrayList<BeanAlunoFone>();

		String sql = " SELECT nome, email, numero, tipo FROM telefonealuno AS fone ";
		sql+=" INNER JOIN alunojdbc AS aluno ";
		sql+=" ON fone.usuario = aluno.id ";
		sql+=" WHERE aluno.id = "+idAluno;

		PreparedStatement statement = connection.prepareStatement(sql);

		ResultSet resultado = statement.executeQuery();

		while (resultado.next()) {
			BeanAlunoFone beanAlunoFone = new BeanAlunoFone();
			beanAlunoFone.setNome(resultado.getString("nome"));
			beanAlunoFone.setNumero(resultado.getString("numero"));
			beanAlunoFone.setEmail(resultado.getString("email"));
			beanAlunoFone.setTipo(resultado.getString("tipo"));

			list.add(beanAlunoFone);
		}

		return list;
	}

	public void deletarCascata(Long id) {
		try {
			String sqltelefone = "DELETE FROM telefonealuno WHERE usuario = "+id;
			String sqlaluno = "DELETE FROM alunojdbc WHERE id = "+id;

			PreparedStatement statement = connection.prepareStatement(sqltelefone);
			statement.execute();
			connection.commit();

			statement = connection.prepareStatement(sqlaluno);
			statement.execute();
			connection.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public String ultimoRegistro() throws SQLException {

		//Retorna toda  a linha do último id
		//String sql = "SELECT * FROM alunojdbc WHERE id = (SELECT MAX(id) FROM alunojdbc)";
		String sql = "SELECT * FROM alunojdbc ORDER BY id DESC LIMIT 1";

		PreparedStatement preparedStatement = connection.prepareStatement(sql); 
		ResultSet resultSet = preparedStatement.executeQuery();

		//como vai retornar apenas um registro, nao precisa do while
		resultSet.next(); 
		String ultimoRegistro = resultSet.getString("id");
		//Se precisar retornar um int ou outra forma, converter(Mudar método e retorno)
		//int id = Integer.parseInt(ultimoRegistro);

		resultSet.close();
		preparedStatement.close();
		connection.close();

		return ultimoRegistro;
	}


}
