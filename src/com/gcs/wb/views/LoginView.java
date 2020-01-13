package com.gcs.wb.views;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.controller.LoginController;
import com.sap.conn.jco.JCoException;
import java.awt.Color;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.hibersap.HibersapException;
import org.hibersap.session.Session;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;

public class LoginView extends javax.swing.JDialog implements IWeighBridge {
    private javax.swing.JButton btnLogin;
    private org.jdesktop.swingx.JXBusyLabel iconLoading;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPanel pnForm;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    private boolean authenticated = false;
    private boolean validUsername = false;
    private boolean validPassword = false;

    public LoginView(java.awt.Frame parent) {
        super(parent);
        initLoginComponent();
        initLoginForm();
    }

    private void initLoginComponent() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class).getContext().getActionMap(LoginView.class, this);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class).getContext().getResourceMap(LoginView.class);

        initLoginDialog(resourceMap);
        initLoginPanel();
        initLoginButton(actionMap, resourceMap);
        initUserNameLabel(resourceMap);
        initUserNameInput(resourceMap);
        initPasswordLabel(resourceMap);
        initPasswordInput(actionMap);
        initBindingGroup();
        initLoadingIcon();
        initLayout();
    }

    private void initLayout() {
        GroupLayout pnFormLayout = new GroupLayout(pnForm);
        pnForm.setLayout(pnFormLayout);
        pnFormLayout.setHorizontalGroup(
            pnFormLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(pnFormLayout.createSequentialGroup()
                .addGroup(pnFormLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(pnFormLayout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addGroup(pnFormLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPassword)
                            .addComponent(txtUsername, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                            .addGroup(pnFormLayout.createSequentialGroup()
                                .addComponent(btnLogin)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(iconLoading, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                    .addGroup(pnFormLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(pnFormLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                            .addComponent(lblPassword, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblUsername))))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        pnFormLayout.setVerticalGroup(
            pnFormLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(pnFormLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(pnFormLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblUsername)
                    .addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnFormLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPassword)
                    .addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnFormLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(btnLogin)
                    .addComponent(iconLoading, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnForm, GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnForm, GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                .addContainerGap())
        );

        bindingGroup.bind();

        pack();
    }

    private void initLoadingIcon() {
        iconLoading = new org.jdesktop.swingx.JXBusyLabel();
        iconLoading.setVisible(false);
        iconLoading.setBusy(true);
        iconLoading.setLabelFor(btnLogin);
        iconLoading.setDirection(org.jdesktop.swingx.JXBusyLabel.Direction.RIGHT);
        iconLoading.setName("iconLoading");
    }

    private void initBindingGroup() {
        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, txtUsername, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), txtPassword, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();
        bindingGroup.addBinding(binding);
    }

    private void initPasswordInput(ActionMap actionMap) {
        txtPassword = new JPasswordField();
        txtPassword.setAction(actionMap.get("login"));
        txtPassword.setName("txtPassword");
        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPasswordFocusGained(evt);
            }
        });
    }

    private void initPasswordLabel(ResourceMap resourceMap) {
        lblPassword = new JLabel();
        lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
        lblPassword.setText(resourceMap.getString("lblPassword.text"));
        lblPassword.setName("lblPassword");
    }

    private void initUserNameInput(ResourceMap resourceMap) {
        txtUsername = new JTextField();
        txtUsername.setForeground(resourceMap.getColor("txtUsername.foreground"));
        txtUsername.setName("txtUsername");
        txtUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsernameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUsernameFocusLost(evt);
            }
        });
    }

    private void initUserNameLabel(ResourceMap resourceMap) {
        lblUsername = new JLabel();
        lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
        lblUsername.setText(resourceMap.getString("lblUsername.text"));
        lblUsername.setName("lblUsername");
    }

    private void initLoginButton(ActionMap actionMap, ResourceMap resourceMap) {
        btnLogin = new JButton();
        btnLogin.setAction(actionMap.get("login"));
        btnLogin.setText(resourceMap.getString("btnLogin.text"));
        btnLogin.setName("btnLogin");
    }

    private void initLoginDialog(ResourceMap resourceMap) {
        setTitle(resourceMap.getString("Form.title"));
        setFont(resourceMap.getFont("Form.font"));
        setModal(true);
        setName("Form");
        setResizable(false);
    }

    private void initLoginPanel() {
        pnForm = new javax.swing.JPanel();
        pnForm.setName("pnForm");
        pnForm.setPreferredSize(new java.awt.Dimension(332, 139));
    }

    private void txtUsernameFocusGained(java.awt.event.FocusEvent evt) {
        txtUsername.selectAll();
    }

    private void txtPasswordFocusGained(java.awt.event.FocusEvent evt) {
        txtPassword.selectAll();
    }

    private void txtUsernameFocusLost(java.awt.event.FocusEvent evt) {
        txtUsername.setText(txtUsername.getText().toUpperCase());
    }

    private void initLoginForm() {
        txtUsername.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateUsername(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateUsername(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateUsername(e);
            }
        });

        txtPassword.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validatePassword(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validatePassword(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validatePassword(e);
            }
        });

        validateForm();
    }

    private void validateUsername(DocumentEvent e) {
        try {
            String text = e.getDocument().getText(0, e.getDocument().getLength()).trim();
            validUsername = text.length() > 0 && text.length() <= 12;
            lblUsername.setForeground(validUsername ? Color.BLACK : Color.RED);
        } catch (BadLocationException ex) {
            Logger.getLogger(LoginView.class.getName()).log(Level.ERROR, null, ex);
        } finally {
            validateForm();
        }
    }

    private void validatePassword(DocumentEvent e) {
        try {
            String text = e.getDocument().getText(0, e.getDocument().getLength()).trim();
            validPassword = text.length() > 0;
            lblPassword.setForeground(validPassword ? Color.BLACK : Color.RED);
        } catch (BadLocationException ex) {
            Logger.getLogger(LoginView.class.getName()).log(Level.ERROR, null, ex);
        } finally {
            validateForm();
        }
    }

    public void validateForm() {
        btnLogin.setEnabled(validUsername && validPassword);
    }

    public boolean isAuthenticated() {
        return this.authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    @Action()
    public Task login() {
        return new LoginTask(WeighBridgeApp.getApplication());
    }

    private class LoginTask extends org.jdesktop.application.Task<Object, Void> {

        private LoginController loginController;

        LoginTask(Application app) {
            super(app);
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            loginController = new LoginController(username, password);

            txtUsername.setEnabled(false);
            txtPassword.setEnabled(false);
            iconLoading.setVisible(true);
            btnLogin.setEnabled(false);
        }

        @Override
        protected Object doInBackground() throws Exception {
            try {
                return loginController.doLogin();
            } catch (Exception ex) {
                throw ex;
            }
        }

        @Override
        protected void succeeded(Object result) {
            setAuthenticated(true);
            WeighBridgeApp.getApplication().setCredentials(loginController.getCredentials());
            WeighBridgeApp.getApplication().setLogin(loginController.getUser());
            WeighBridgeApp.getApplication().setOfflineMode(loginController.isOfflineMode());
            WeighBridgeApp.getApplication().setSapSetting(loginController.getSapSetting());
            WeighBridgeApp.getApplication().getConfig().setModeNormal(loginController.isOfflineMode());
            setVisible(false);
        }

        @Override
        protected void failed(Throwable cause) {
            if (cause instanceof HibersapException && cause.getCause() instanceof JCoException) {
                cause = cause.getCause();
                Session session = WeighBridgeApp.getApplication().getSAPSession();
                if (session != null) {
                    session.close();
                    session = null;
                }
                WeighBridgeApp.getApplication().setSAPSession(session);
            }

            txtUsername.setEnabled(true);
            txtPassword.setEnabled(true);
            iconLoading.setVisible(false);
            btnLogin.setEnabled(true);

            Logger.getLogger(LoginView.class.getName()).log(Level.ERROR, null, cause);
            JOptionPane.showMessageDialog(rootPane, cause.getMessage());
        }
    }
}
