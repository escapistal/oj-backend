package com.xc.oj.service;

import com.xc.oj.entity.Problem;
import com.xc.oj.entity.User;
import com.xc.oj.entity.UserInfo;
import com.xc.oj.response.responseBase;
import com.xc.oj.repository.UserRepository;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import com.xc.oj.util.AuthUtil;
import com.xc.oj.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService  {

    private final UserRepository userRepository;
    private final ProblemService problemService;
    private final AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
    private Pattern passwordPattern=Pattern.compile("^[\\w~`!@#$%^&*()_+-=/?\\|<>,.;\\[\\]{}'\":]{6,16}$");
    private Pattern usernamePattern=Pattern.compile("^[\\w]{3,16}$");
    private Pattern emailPattern=Pattern.compile("^[\\w-_]+@[\\w.-]+$");

    public UserService(UserRepository userRepository, ProblemService problemService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.problemService = problemService;
        this.authenticationManager = authenticationManager;
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public void save(User user){
        userRepository.save(user);
    }

    public responseBase<List<User>> list() {
        List<User> users=userRepository.findAll();
        users.forEach(user->{user.setPassword(null);user.setAcceptedId(null);});
        users=users.stream().filter(user->!user.getRole().contains("admin")).collect(Collectors.toList());
        Collections.sort(users);
        return responseBuilder.success(users);
    }

    public responseBase<User> get(Long id) {
        User user=userRepository.findById(id).orElse(null);
        if(user==null)
            return responseBuilder.fail(responseCode.USER_NOT_EXIST);
        user.setPassword(null);
        return responseBuilder.success(user);
    }

    public responseBase<String> add(User user){//admin专用register
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreateTime(new Timestamp(new Date().getTime()));
        user.setAcceptedId(new ArrayList<>());
        user.setAcceptedNumber(0);
        user.setSubmissionNumber(0);
        userRepository.save(user);
        return responseBuilder.success();
    }

    public responseBase<String> register(String username, String password, String email,List<String> role) {
        if (!usernamePattern.matcher(username).matches())
            return responseBuilder.fail(responseCode.USERNAME_INVALID);
        if (!passwordPattern.matcher(password).matches())
            return responseBuilder.fail(responseCode.PASSWORD_INVALID);
        if (!emailPattern.matcher(email).matches())
            return responseBuilder.fail(responseCode.EMAIL_INVALID);
        if (userRepository.existsByUsername(username))
            return responseBuilder.fail(responseCode.USERNAME_EXISTS);
        if (userRepository.existsByEmail(email))
            return responseBuilder.fail(responseCode.EMAIL_EXISTS);
        User user=new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password));
        user.setEmail(email);
        user.setNickname(user.getUsername());
        user.setRealname(user.getUsername());
        user.setAcceptedId(new ArrayList<>());
        user.setAcceptedNumber(0);
        user.setSubmissionNumber(0);
        user.setRole(role);
        user.setDisabled(false);
        user.setCreateTime(new Timestamp(new Date().getTime()));
        userRepository.save(user);
        return responseBuilder.success();
    }

    public responseBase<UserInfo> login(String username, String password) {
        System.out.println(username+" "+password);
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String,Object> claims=new HashMap<>();
        List<User> users=userRepository.findByUsername(username);
        User user=users.get(0);
        claims.put("userId",user.getId());
        claims.put("userRole",user.getRole());
        final String token = JWTUtil.create(claims);
        user.setLastLoginTime(new Timestamp(new Date().getTime()));
        userRepository.save(user);
        UserInfo userInfo=new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setRole(user.getRole());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname());
        userInfo.setRealname(user.getRealname());
        userInfo.setToken(token);
        return responseBuilder.success(userInfo);
    }

    public responseBase<User> update(long id, User user){
        User data=userRepository.findById(id).orElse(null);
        if(data==null)
            return responseBuilder.fail(responseCode.USER_NOT_EXIST);
        if(user.getEmail()!=null)
            data.setEmail(user.getEmail());
        if(user.getAvatar()!=null)
            data.setAvatar(user.getAvatar());
        if(user.getNickname()!=null)
            data.setNickname(user.getNickname());
        if(user.getRealname()!=null)
            data.setRealname(user.getRealname());
        if(AuthUtil.has("admin")){//拥有admin权限才能修改以下字段
            if(user.getRole()!=null)
                data.setRole(user.getRole());
            if(user.getDisabled()!=null)
                data.setDisabled(user.getDisabled());
        }
        userRepository.save(data);
        data.setPassword(null);
        return responseBuilder.success(data);
    }

    public responseBase<String> add_submission_result(long uid,long pid,boolean isAc){//是题库的pid，比赛走add_contest_submission_accepted
        User user=userRepository.findById(uid).orElse(null);
        if(user==null)
            return responseBuilder.fail(responseCode.USER_NOT_EXIST);
        Problem prob=problemService.findById(pid).orElse(null);
        if(prob==null)
            return responseBuilder.fail(responseCode.PROBLEM_NOT_EXIST);
        if(isAc) {
            List<Long> accId = user.getAcceptedId();
            if (!accId.contains(pid)) {
                accId.add(pid);
                user.setAcceptedId(accId);
                user.setAcceptedNumber(user.getAcceptedNumber() + 1);
                prob.setAcceptedNumber(prob.getAcceptedNumber() + 1);
            }
        }
        user.setSubmissionNumber(user.getSubmissionNumber()+1);
        prob.setSubmissionNumber(prob.getSubmissionNumber()+1);
        userRepository.save(user);
        problemService.save(prob);
        return responseBuilder.success();
    }

    public responseBase<String> delete(long id){
        User user=userRepository.findById(id).orElse(null);
        if(user==null)
            return responseBuilder.fail(responseCode.USER_NOT_EXIST);
        userRepository.deleteById(id);
        return responseBuilder.success();
    }

}
