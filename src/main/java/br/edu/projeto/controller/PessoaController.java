package br.edu.projeto.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import br.edu.projeto.dao.PessoaDAO;
import br.edu.projeto.model.Pessoa;

//Escopo do objeto da classe (Bean)
//ApplicationScoped é usado quando o objeto é único na aplicação (compartilhado entre usuários), permanece ativo enquanto a aplicação estiver ativa
//SessionScoped é usado quando o objeto é único por usuário, permanece ativo enquanto a sessão for ativa
//ViewScoped é usado quando o objeto permanece ativo enquanto não houver um redirect (acesso a nova página)
//RequestScoped é usado quando o objeto só existe naquela requisição específica
//Quanto maior o escopo, maior o uso de memória no lado do servidor (objeto permanece ativo por mais tempo)
//Escopos maiores que Request exigem que classes sejam serializáveis assim como todos os seus atributos (recurso de segurança)
@ViewScoped
//Torna classe disponível na camada de visão (html) - são chamados de Beans ou ManagedBeans (gerenciados pelo JSF/EJB)
@Named
public class PessoaController implements Serializable {
	private static final long serialVersionUID = 1L;

	//Anotação que marca atributo para ser gerenciado pelo CDI
	//O CDI criará uma instância do objeto automaticamente quando necessário
	@Inject
	private FacesContext facesContext;
	
	@Inject
    private PessoaDAO pessoaDAO;
	
	private Pessoa pessoa;
	
	private List<Pessoa> listaPessoa;
	
	private Boolean rendNovoCadastro;
	
	//Anotação que força execução do método após o construtor da classe ser executado
    @PostConstruct
    public void init() {
    	//Inicializa elementos importantes
    	this.setListaPessoa(pessoaDAO.listAll());
    }
	
    //Chamado pelo botão novo
	public void novoCadastro() {
        this.setPessoa(new Pessoa());
        this.setRendNovoCadastro(true);
    }
	
	//Chamado pelo botão alterar
	public void alterarCadastro() {
        this.setRendNovoCadastro(false);
    }
	
	//Chamado pelo botão remover da tabela
	public void remover() {
		if (this.pessoaDAO.delete(this.pessoa)) {
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Pessoa Removida", null));
			this.listaPessoa.remove(this.pessoa);
		} else 
			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Remover Camiseta", null));
		//Após excluir usuário é necessário recarregar lista que popula tabela com os novos dados
		//this.listaCamiseta = pessoaDAO.listAll();
        //Limpa seleção de usuário
		this.pessoa = null;
        PrimeFaces.current().ajax().update("form:messages", "form:dt-pessoas");
	}	
	
	//Chamado ao salvar cadastro de usuário (novo)
//	public void salvarNovo() {
//		if (this.pessoa.getTamanho().equals("P") || this.pessoa.getTamanho().equals("M") || this.pessoa.getTamanho().equals("G"))
//		{
//			if (this.pessoaDAO.insert(this.pessoa)) {
//				this.getListaCamiseta().add(this.pessoa);
//				PrimeFaces.current().executeScript("PF('pessoaDialog').hide()");
//				PrimeFaces.current().ajax().update("form:dt-pessoa");
//				this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Camiseta Criada", null));
//			} else
//        		this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Criar Camiseta", null));
//		} else {
//			this.facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Tamanho da pessoa inválido, deve ser P, M ou G!", null));
//    	}   
//		PrimeFaces.current().ajax().update("form:messages");
//	}
	public void salvarNovaPessoa() {
		if (this.pessoaDAO.insert(this.pessoa)) {
			this.getListaPessoa().add(this.pessoa);
			PrimeFaces.current().executeScript("PF('clientDialog').hide()");
			PrimeFaces.current().ajax().update("form:dt-client");
			this.facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente Registrado", null));
		} else
			this.facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Registar o Cliente", null));
		PrimeFaces.current().ajax().update("form:messages");
	}
	
	//Chamado ao salvar cadastro de usuário (alteracao)
	public void saveUpdate() {
		if (this.pessoaDAO.update(this.pessoa)) {
			PrimeFaces.current().executeScript("PF('clientDialog').hide()");
			PrimeFaces.current().ajax().update("form:dt-client");
			this.facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente Atualizado", null));
		} else
			this.facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao Atualizar o Cliente", null));
		this.setListaPessoa(pessoaDAO.listAll());
		PrimeFaces.current().ajax().update("form:messages");
	}
	
	//GETs e SETs
	//GETs são necessários para elementos visíveis em tela
	//SETs são necessários para elementos que serão editados em tela
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<Pessoa> getListaPessoa() {
		return listaPessoa;
	}

	public void setListaPessoa(List<Pessoa> listaPessoa) {
		this.listaPessoa= listaPessoa;
	}

	public Boolean getRendNovoCadastro() {
		return rendNovoCadastro;
	}

	public void setRendNovoCadastro(Boolean rendNovoCadastro) {
		this.rendNovoCadastro = rendNovoCadastro;
	}
}