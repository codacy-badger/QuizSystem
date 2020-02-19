package ru.dgi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import ru.dgi.util.exception.NotFoundException;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Configuration
public class AuthorizedUser {
    private static final long serialVersionUID = 1L;

    private static List<String> SPRING_PROFILES_ACTIVE;

    @Autowired
    public AuthorizedUser(@Value("${spring.profiles.active}") String[] actProfiles) {
        SPRING_PROFILES_ACTIVE = Arrays.asList(actProfiles);
    }

    private static LdapUserDetailsImpl safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        LdapUserDetailsImpl principal = (LdapUserDetailsImpl)auth.getPrincipal();
        return principal;
    }

    private static LdapUserDetailsImpl get() {
        LdapUserDetailsImpl user = safeGet();
        requireNonNull(user, "No authorized user found");
        return user;
    }

    public static String getUserName() {
        if (SPRING_PROFILES_ACTIVE.contains("dev"))
        {
            return ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        }
        return get().getUsername();
    }

    public static String getFullName() {
        if (SPRING_PROFILES_ACTIVE.contains("dev"))
        {
            return "None";
        }

        String fullName = null;
        try {
            String dn = safeGet().getDn();
            LdapName ln = new LdapName(dn);
            for (Rdn rdn : ln.getRdns()) {
                if (rdn.getType().equalsIgnoreCase("CN")) {
                    fullName = rdn.getValue().toString();
                    break;
                }
            }
        } catch (InvalidNameException e) {
            throw new NotFoundException("Invalid read FullName CN from LDAP");
        }
        return requireNonNull(fullName);
    }
}
