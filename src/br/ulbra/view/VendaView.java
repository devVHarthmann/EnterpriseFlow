/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ulbra.view;

import br.ulbra.controller.ClienteController;
import br.ulbra.controller.ItensVendaController;
import br.ulbra.controller.VendaController;
import br.ulbra.controller.ProdutoController;

import br.ulbra.model.Produto;
import br.ulbra.model.Usuario;
import br.ulbra.model.Cliente;
import br.ulbra.model.ItensVenda;
import br.ulbra.model.Venda;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author aluno.saolucas
 */
public class VendaView extends javax.swing.JFrame {

    /**
     * Creates new form VendaView
     */
    public VendaView() {
        initComponents();
        controller = new VendaController();
        controller2 = new ItensVendaController();
        controller3 = new ClienteController();
        controller4 = new ProdutoController();
        this.setLocationRelativeTo(null);
        carregarProdutos();
        carregarVendedores();
        initTableModel();
        setBotoes(0);

        limparSelecoes();
    }

    private void limparSelecoes() {
        // cbProduto e cbVendedor podem ficar “sem seleção”
        cbProduto.setSelectedItem(null);
        cbVendedor.setSelectedItem(null);

        // cbCategoria começa em “Todas” (sempre existe)
        cbCategoria.setSelectedItem("Todas");
    }

    private ClienteController controller3;
    private VendaController controller;
    private ItensVendaController controller2;
    private boolean atualizandoProdutos = false;
    private List<Usuario> listaVendedor = new ArrayList<>();
    private List<Produto> listaProdutos = new ArrayList<>();
    private int idClienteD;
    private ProdutoController controller4;

    private void carregarProdutos() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbenterpriseflow", "root", "")) {

            String sql = "SELECT DISTINCT idProduto, nomeProduto, categoria FROM produto";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            listaProdutos.clear();
            cbProduto.removeAllItems();
            cbCategoria.removeAllItems();

            Set<String> categorias = new HashSet<>();

            while (rs.next()) {
                Produto p = new Produto(
                        rs.getInt("idProduto"),
                        rs.getString("nomeProduto"),
                        rs.getString("categoria")
                );
                listaProdutos.add(p);
                categorias.add(p.getCategoria());
            }

            // ✅ Item "Todas" para facilitar filtro
            cbCategoria.addItem("Todas");
            for (String cat : categorias) {
                cbCategoria.addItem(cat);
            }

            // ✅ Adiciona produtos, mas sem seleção automática
            for (Produto p : listaProdutos) {
                cbProduto.addItem(p.getNomeProduto());
            }

            rs.close();
            ps.close();

            // ✅ Listeners (coloque após popular)
            cbCategoria.addActionListener(e -> filtrarProdutosPorCategoria());
            cbProduto.addActionListener(e -> atualizarCategoriaPeloProduto());

            // ✅ Deixa combos “sem seleção”
            cbProduto.setSelectedItem(null);
            cbCategoria.setSelectedItem("Todas");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void filtrarProdutosPorCategoria() {
        String categoriaSelecionada = (String) cbCategoria.getSelectedItem();

        // Evita que o listener de produto dispare enquanto recarrega o combo
        atualizandoProdutos = true;
        cbProduto.removeAllItems();

        if (categoriaSelecionada == null || categoriaSelecionada.equals("Todas")) {
            for (Produto p : listaProdutos) {
                cbProduto.addItem(p.getNomeProduto());
            }
        } else {
            for (Produto p : listaProdutos) {
                if (p.getCategoria().equalsIgnoreCase(categoriaSelecionada)) {
                    cbProduto.addItem(p.getNomeProduto());
                }
            }
        }

        atualizandoProdutos = false;
        cbProduto.setSelectedItem(null);
    }

    private void atualizarCategoriaPeloProduto() {
        if (atualizandoProdutos) {
            return; // ignora evento enquanto produtos estão sendo recarregados
        }
        String produtoSelecionado = (String) cbProduto.getSelectedItem();
        String categoriaSelecionada = (String) cbCategoria.getSelectedItem();

        if (produtoSelecionado == null || produtoSelecionado.isEmpty()) {
            return;
        }

        if (categoriaSelecionada == null || categoriaSelecionada.equals("Todas") || categoriaSelecionada.isEmpty()) {
            for (Produto p : listaProdutos) {
                if (p.getNomeProduto().equals(produtoSelecionado)) {
                    cbCategoria.setSelectedItem(p.getCategoria());
                    break;
                }
            }
        }
    }

    private void carregarVendedores() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbenterpriseflow", "root", "");
            String sql = "SELECT DISTINCT idusuario, nome FROM usuario";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            cbVendedor.removeAllItems();

            while (rs.next()) {
                Usuario p = new Usuario(rs.getInt("idusuario"), rs.getString("nome"));
                listaVendedor.add(p);
                cbVendedor.addItem(p.getNome());
            }

            rs.close();
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar vendedores: " + e.getMessage());
        }
    }

    private void initTableModel() {
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"Produto", "Categoria", "Valor unitário", "Unidades"}
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tbVenda.setModel(model);
    }

    public void setBotoes(int op) {
        switch (op) {
            case 1:
                btnSalvar.setEnabled(false);

                btnExcluir.setEnabled(true);
                break;
            default:
                btnSalvar.setEnabled(true);

                btnExcluir.setEnabled(false);

        }
    }

    private void limparCampos() {
        txtNome.setText(null);

        txtCpf.setText(null);
        jspUnidades.setValue(0);
        cbProduto.setSelectedIndex(-1);
        cbCategoria.setSelectedIndex(-1);
        dcDataVenda.setDate(null);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        txtValorTotal = new javax.swing.JTextField();
        btnSalvar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCpf = new javax.swing.JTextField();
        try{
            javax.swing.text.MaskFormatter cpf = new javax.swing.text.MaskFormatter("###.###.###-##");

            txtCpf = new javax.swing.JFormattedTextField(cpf);
        }catch(Exception e){
        }
        jLabel3 = new javax.swing.JLabel();
        dcDataVenda = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbProduto = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbVenda = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        cbCategoria = new javax.swing.JComboBox<>();
        txtNome = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        btnProcurar = new javax.swing.JToggleButton();
        btnLimparCampos = new javax.swing.JToggleButton();
        btnLimparTabela = new javax.swing.JToggleButton();
        btnAdicionarItem = new javax.swing.JToggleButton();
        btnExcluir = new javax.swing.JButton();
        cbVendedor = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        btnVoltar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jspUnidades = new javax.swing.JSpinner();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        txtValorTotal.setEditable(false);
        txtValorTotal.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        txtValorTotal.setText("VALOR TOTAL:");
        txtValorTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorTotalActionPerformed(evt);
            }
        });

        btnSalvar.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btnSalvar.setText("Cadastrar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel1.setText("CPF cliente:");

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel7.setText("Vendedor");

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel2.setText("Cadastro de venda");

        txtCpf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCpfActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setText("Data de venda:");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setText("Produto:");

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel8.setText("Unidades de compra:");

        tbVenda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Produto", "Categoria:", "Valor unitário em R$", "Unidades"
            }
        ));
        jScrollPane1.setViewportView(tbVenda);

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel9.setText("Categoria:");

        txtNome.setEditable(false);
        txtNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel10.setText("Nome cliente:");

        btnProcurar.setText("Procurar CPF");
        btnProcurar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcurarActionPerformed(evt);
            }
        });

        btnLimparCampos.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnLimparCampos.setText("Limpar campos");
        btnLimparCampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparCamposActionPerformed(evt);
            }
        });

        btnLimparTabela.setText("Limpar tabela");
        btnLimparTabela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparTabelaActionPerformed(evt);
            }
        });

        btnAdicionarItem.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnAdicionarItem.setText("Adicionar item");
        btnAdicionarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarItemActionPerformed(evt);
            }
        });

        btnExcluir.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });

        btnVoltar.setText("Voltar");
        btnVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVoltarActionPerformed(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/src/622414_buy_cart_shopping_basket_ecommerce_icon.png"))); // NOI18N

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jLabel7))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(cbVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnAdicionarItem, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(55, 55, 55)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnLimparCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jspUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel9)
                                            .addComponent(cbProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(55, 55, 55)
                                        .addComponent(jLabel8))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6))
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(btnProcurar))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(139, 139, 139)
                                .addComponent(jLabel3))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(dcDataVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLimparTabela)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtValorTotal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnVoltar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(21, 21, 21))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel2)
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProcurar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel10))
                    .addComponent(jLabel3))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dcDataVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addGap(6, 6, 6)
                .addComponent(cbVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jLabel4)
                .addGap(3, 3, 3)
                .addComponent(cbProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel9))
                    .addComponent(jLabel8))
                .addGap(3, 3, 3)
                .addComponent(cbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnLimparCampos, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(btnAdicionarItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(jLabel6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(85, 85, 85)
                                .addComponent(btnLimparTabela, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addComponent(txtValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnVoltar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnExcluir, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                                    .addComponent(btnSalvar, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(314, 314, 314)
                                .addComponent(jspUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(83, 83, 83))))
                    .addComponent(jSeparator2))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtValorTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorTotalActionPerformed

    private void btnLimparTabelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparTabelaActionPerformed
        ((DefaultTableModel) tbVenda.getModel()).setRowCount(0);
    }//GEN-LAST:event_btnLimparTabelaActionPerformed

    private void btnProcurarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcurarActionPerformed
        try {
            String cpf = txtCpf.getText().toString().trim();
            String nome = controller3.buscarCpf(cpf).getNome();
            txtNome.setText(nome);
            idClienteD = controller3.buscarCpf(cpf).getIdCliente();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "CPF não cadastrado");
            txtNome.setText(null);
        }
    }//GEN-LAST:event_btnProcurarActionPerformed

    private void txtCpfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCpfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCpfActionPerformed

    private void txtNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnAdicionarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarItemActionPerformed
        boolean verificar = true;
        String produto = cbProduto.getSelectedItem().toString();
        String cat = cbCategoria.getSelectedItem().toString();
        int quantProd = (int) jspUnidades.getValue();
        int quantEstoque = 0;
        double precoTotal = 0;
        Double valorUnit = null;
        try {

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbenterpriseflow", "root", "");
            String sql = "SELECT valorUnitario, quantEstoque FROM produto WHERE nomeProduto = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, produto);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                valorUnit = rs.getDouble("valorUnitario");
                quantEstoque = rs.getInt("quantEstoque");
            }
        } catch (Exception e) {

        }

        javax.swing.table.DefaultTableModel modelTbl = (javax.swing.table.DefaultTableModel) tbVenda.getModel();

        for (int i = 0; i < modelTbl.getRowCount(); i++) {
            String teste = modelTbl.getValueAt(i, 0).toString();
            if (teste == produto) {
                verificar = false;
            }
        }
        if (verificar) {
            if (quantEstoque >= quantProd && quantProd > 0) {
                modelTbl.addRow(new Object[]{produto, cat, valorUnit, quantProd});
                for (int i = 0; i < modelTbl.getRowCount(); i++) {
                    int q = Integer.parseInt(tbVenda.getValueAt(i, 3).toString());
                    double pu = Double.parseDouble(tbVenda.getValueAt(i, 2).toString());
                    precoTotal += q * pu;
                }
                txtValorTotal.setText("VALOR TOTAL: R$" + precoTotal);
            } else {
                JOptionPane.showMessageDialog(null, "Quantidade no estoque é de " + quantEstoque + " unidades. A quantidade de produtos deve ser maior que 0");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Esse produto já foi cadastrado na venda!");
        }
    }//GEN-LAST:event_btnAdicionarItemActionPerformed

    private void btnLimparCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparCamposActionPerformed
        limparCampos();
    }//GEN-LAST:event_btnLimparCamposActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        int idVendedor = 0;
        String nomeVendedor = "";
        int indice = cbVendedor.getSelectedIndex();
        int quantP = 0;
        int novaQuant = 0;
        javax.swing.table.DefaultTableModel modelTbl = (javax.swing.table.DefaultTableModel) tbVenda.getModel();
        if (indice >= 0) {
            Usuario usuario = listaVendedor.get(indice);
            idVendedor = usuario.getId_usuario();
            nomeVendedor = usuario.getNome();
        }

        try {
            double precoTotal = 0;
            String nome = txtNome.getText().trim();
            for (int i = 0; i < modelTbl.getRowCount(); i++) {
                int q = Integer.parseInt(tbVenda.getValueAt(i, 3).toString());
                double pu = Double.parseDouble(tbVenda.getValueAt(i, 2).toString());
                precoTotal += q * pu;
            }
            Date prazoT = dcDataVenda.getDate();
            //Vamo xiruzão
            LocalDate dataVenda = prazoT.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            Cliente c = new Cliente(idClienteD, txtNome.getText());
            Usuario u = new Usuario(idVendedor, nomeVendedor);
            if (nome.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Nome é obrigatório");
                return;
            }
            Venda v = new Venda(0, dataVenda, precoTotal, u, c);
            controller.salvar(v);

            javax.swing.JOptionPane.showMessageDialog(this, "Cadastrado com sucesso");
            ////////////////////////////////////////////////////////////////////////////////////////////

            for (int i = 0; i < modelTbl.getRowCount(); i++) {
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbenterpriseflow", "root", "");
                String produto = tbVenda.getValueAt(i, 0).toString();
                int quant = Integer.parseInt(tbVenda.getValueAt(i, 3).toString());
                double precoU = Double.parseDouble(tbVenda.getValueAt(i, 2).toString());
                int idProduto = 0;
                String sql = "SELECT idProduto, quantEstoque FROM produto WHERE nomeProduto = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, produto);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    idProduto = rs.getInt("idProduto");
                    quantP = rs.getInt("quantEstoque");
                    novaQuant = quantP - quant;
                }
                Produto p = new Produto(idProduto, produto, novaQuant);
                ItensVenda iv = new ItensVenda(v, p, quant, precoU);
                controller2.salvar(iv);

                controller4.atualizarQuantidade(p);

            }
            ////////////////////////////////////////////////////////////////////////////////////////////
            limparCampos();
            precoTotal = 0;
            ((DefaultTableModel) tbVenda.getModel()).setRowCount(0);
            txtValorTotal.setText("VALOR TOTAL:");
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
            ex.printStackTrace(); // mostra tudo no console
            StackTraceElement linha = ex.getStackTrace()[0];

        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltarActionPerformed
        new Dashboard().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnVoltarActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VendaView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VendaView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VendaView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VendaView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VendaView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnAdicionarItem;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JToggleButton btnLimparCampos;
    private javax.swing.JToggleButton btnLimparTabela;
    private javax.swing.JToggleButton btnProcurar;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnVoltar;
    private javax.swing.JComboBox<String> cbCategoria;
    private javax.swing.JComboBox<String> cbProduto;
    private javax.swing.JComboBox<String> cbVendedor;
    private com.toedter.calendar.JDateChooser dcDataVenda;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JSpinner jspUnidades;
    private javax.swing.JTable tbVenda;
    private javax.swing.JTextField txtCpf;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtValorTotal;
    // End of variables declaration//GEN-END:variables
}
