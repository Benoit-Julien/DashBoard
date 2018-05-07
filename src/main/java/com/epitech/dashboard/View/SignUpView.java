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

@SpringView(name = SignUpView.VIEW_NAME)
public class SignUpView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "sign-up";

    private final ErrorMessage firtNameError = new UserError("Ne peut pas être vide");
    private final ErrorMessage lastNameError = new UserError("Ne peut pas être vide");
    private final ErrorMessage pseudoError = new UserError("Ne peut pas être vide");
    private final ErrorMessage passwordError = new UserError("Ne peut pas être vide");
    private final ErrorMessage passwordConfError = new UserError("Ne correspond pas");

    @Autowired
    private UserRepository userRepository;

    private void ShowError(AbstractComponent component, ErrorMessage message)
    {
        component.setComponentError(message);
    }
    private void HideError(AbstractComponent component)
    {
        component.setComponentError(null);
    }

    private void SignUp(User user)
    {
        if (!userRepository.findByPseudo(user.getPseudo()).isEmpty()) {
            Notification.show("Ce compte existe déjà");
            return;
        }
        userRepository.save(user);
        DashBoardView.currentUser = user;
        getUI().getNavigator().navigateTo(DashBoardView.VIEW_NAME);
    }

    @PostConstruct
    private void init() {
        Label label = new Label("Créer un compte");
        TextField firstName = new TextField("Prénom :");
        TextField lastName = new TextField("Nom :");
        TextField pseudo = new TextField("Pseudo :");
        PasswordField password = new PasswordField("Mots de passe :");
        PasswordField passwordConf = new PasswordField("Confirmation :");

        Button signup = new Button("Créer");
        Button login = new Button("Déjà un compte");

        signup.addClickListener(e -> {
            if (firstName.isEmpty())
                this.ShowError(firstName, firtNameError);
            else if (lastName.isEmpty())
                this.ShowError(lastName, lastNameError);
            else if (pseudo.isEmpty())
                this.ShowError(pseudo, pseudoError);
            else if (password.isEmpty())
                this.ShowError(password, passwordError);
            else if (!password.getValue().equals(passwordConf.getValue()))
                this.ShowError(passwordConf, passwordConfError);
            else {
                AES.encrypt(password.getValue());
                String encrypted = AES.getEncryptedString();

                User user = new User(firstName.getValue(), lastName.getValue(), pseudo.getValue(), encrypted);
                this.SignUp(user);
            }
        });
        login.addClickListener(e -> getUI().getNavigator().navigateTo(LoginView.VIEW_NAME));

        firstName.addValueChangeListener(e -> {if (firstName.isEmpty()) this.ShowError(firstName, firtNameError); else this.HideError(firstName);});
        lastName.addValueChangeListener(e -> {if (lastName.isEmpty()) this.ShowError(lastName, lastNameError); else this.HideError(lastName);});
        pseudo.addValueChangeListener(e -> {if (pseudo.isEmpty()) this.ShowError(pseudo, pseudoError); else this.HideError(pseudo);});
        password.addValueChangeListener(e -> {if (password.isEmpty()) this.ShowError(password, passwordError); else this.HideError(password);});
        passwordConf.addValueChangeListener(e-> {if (!passwordConf.getValue().equals(password.getValue())) this.ShowError(passwordConf, passwordConfError); else this.HideError(passwordConf);});

        FormLayout formLayout = new FormLayout(label, firstName, lastName, pseudo, password, passwordConf, signup, login);
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
