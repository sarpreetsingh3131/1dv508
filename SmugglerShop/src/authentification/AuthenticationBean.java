package authentification;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import baseClasses.Order;
import baseClasses.Page;
import baseClasses.User;

@ManagedBean(name = "authenticationBean")
public class AuthenticationBean extends Page {

	public static final String AUTH_KEY = "app.user.name";
	public static User activeUser = new User();

	public User getActiveUser() {
		return activeUser;
	}

	public void setActiveUser(User u) {
		activeUser = u;
	}

	public boolean isLoggedIn() {
		return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AUTH_KEY) != null;
	}

	public void login() {
		Login();
	}

	public void logout() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		ec.getSessionMap().remove(AUTH_KEY);
		activeUser = new User();
		try {
			if (activeUser.getAdmin()) ec.redirect("../mainpage.xhtml");
			else ec.redirect("mainpage.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void Login() {
		System.out.println("Login Try: " + activeUser.getName() + " : " + activeUser.getPassword());
		if (activeUser.getName().isEmpty() || activeUser.getPassword().isEmpty()) {
			super.notify("Error", "Please enter login data");
		} else if ((activeUser.getName().compareTo("root")) == 0
				&& (activeUser.getPassword().compareTo("team2") == 0)) {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(AUTH_KEY, activeUser.getName());
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			try {
				activeUser.setAdmin(true);
				activeUser.setEmail("IntegratedAdmin");
				activeUser.setOrders(new ArrayList<Order>());
				ec.redirect("restricted/adminProducts.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// check Account Data
		else {
			setContent("SELECT * FROM user WHERE name=\"" + activeUser.getName() + "\";");
			// if correct login
			if ((getContent(0, "password").compareTo(activeUser.getPassword()) == 0)) {
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(AUTH_KEY,
						activeUser.getName());
				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
				try {
					activeUser = toUsers(content).get(0);
					// if it is an admin redirect
					if (activeUser.getAdmin()) {
						ec.redirect("restricted/adminProducts.xhtml");
					} else {
						ec.redirect("userAccount.xhtml");
					}

				} catch (IOException | SQLException e) {
					e.printStackTrace();
				}
			}
			System.out.println("not valid:" + getContent(0, "password") + " : " + getContent(0, "admin"));
			super.notify("Invalid Account", "");
		}
	}
}
