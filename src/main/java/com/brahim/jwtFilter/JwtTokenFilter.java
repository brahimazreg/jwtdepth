package com.brahim.jwtFilter;

import com.brahim.jwtUtil.JwtUtil;
import com.brahim.model.User;
import com.brahim.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtil jwtUtil;
   @Autowired
    MyUserService userService;
    String email="";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException  {
        // Authorization Bearer token
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return ;
        }
        String token = authorizationHeader.split(" ")[1].trim();
        if(!jwtUtil.validate(token)){
            filterChain.doFilter(request,response);
            return ;
        }
        // this code retreive roles
         email = jwtUtil.getUsername(token);
        //jwtUtil.getUsername(token);
        try{
            User userdb = userService.getUser(email);
            Collection<GrantedAuthority> autorities = new ArrayList<>();
            userdb.getRoles().forEach(r -> autorities.add(new SimpleGrantedAuthority(r.getName())));
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email,null,autorities);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request,response);
        }catch (Exception e) {
            e.printStackTrace();
            try {
                throw new Exception("User not found");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }



        //code replaced

       // String username = jwtUtil.getUsername(token);
       // UsernamePasswordAuthenticationToken authToken =
              //  new UsernamePasswordAuthenticationToken(username,null,new ArrayList<>());
        //authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
       // SecurityContextHolder.getContext().setAuthentication(authToken);
        //filterChain.doFilter(request,response);
    }


    // code added by Brahim to retreive the current user
    public String getCurrentUser(){
        return email;
    }
}
