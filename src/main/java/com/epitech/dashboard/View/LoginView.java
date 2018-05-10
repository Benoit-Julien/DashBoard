package com.epitech.dashboard.View;

import com.epitech.dashboard.AES;
import com.epitech.dashboard.User;
import com.epitech.dashboard.UserRepository;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.UserError;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringView(name = LoginView.VIEW_NAME)
public class LoginView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "login";

    private final ErrorMessage pseudoError = new UserError("Ne peut pas être vide");
    private final ErrorMessage passwordError = new UserError("Ne peut pas être vide");

    @Autowired
    private UserRepository userRepository;

    private void ShowError(AbstractComponent component, ErrorMessage message) {
        component.setComponentError(message);
    }

    private void HideError(AbstractComponent component) {
        component.setComponentError(null);
    }

    private void Login(String username, String password) {
        AES.encrypt(password);
        String encrypted = AES.getEncryptedString();

        List<User> users = userRepository.findByPseudo(username);

        if (users.isEmpty() || !users.get(0).getPassword().equals(encrypted)) {
            Notification.show("Pseudo ou mots de passe incorrect");
            return;
        }
        DashBoardView.currentUser = users.get(0);
        getUI().getNavigator().navigateTo(DashBoardView.VIEW_NAME);
    }

    @PostConstruct
    private void init() {
        Label title = new Label("Se connecter");
        TextField pseudo = new TextField("Pseudo : ");
        PasswordField password = new PasswordField("Mots de passe : ");

        pseudo.addValueChangeListener(e -> {
            if (pseudo.isEmpty()) this.ShowError(pseudo, pseudoError);
            else this.HideError(pseudo);
        });
        password.addValueChangeListener(e -> {
            if (password.isEmpty()) this.ShowError(password, passwordError);
            else this.HideError(password);
        });

        Button login = new Button("Connexion");
        Button signup = new Button("Créer un compte");

        login.addClickListener(e -> {
            if (pseudo.isEmpty())
                this.ShowError(pseudo, pseudoError);
            else if (password.isEmpty())
                this.ShowError(password, passwordError);
            else
                this.Login(pseudo.getValue(), password.getValue());
        });
        signup.addClickListener(e -> getUI().getNavigator().navigateTo(SignUpView.VIEW_NAME));

        FormLayout formLayout = new FormLayout(title, pseudo, password, login, signup);
        formLayout.setWidth(null);

        setMargin(true);
        setSpacing(true);
        setSizeFull();
        addComponent(formLayout);

        setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
