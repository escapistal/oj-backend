package com.xc.oj.service;

import com.xc.oj.entity.Problem;
import com.xc.oj.entity.UserInfo;
import com.xc.oj.repository.ProblemRepository;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import com.xc.oj.util.AuthUtil;
import com.xc.oj.util.FTPUtil;
import com.xc.oj.util.ZipUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


@Service
public class ProblemService {

    private final ProblemRepository problemRepository;

    public ProblemService(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    public Optional<Problem> findById(Long id){
        return problemRepository.findById(id);
    }

    public void save(Problem problem){
        problemRepository.save(problem);
    }

    public responseBase<Page<Problem>> list(boolean checkVisible, String keyword, List<String> tags, int page, int size) {
        Page<Problem> problems;
        if(!checkVisible&&!AuthUtil.has("admin"))
            checkVisible=true;
        boolean finalCheckVisible = checkVisible;
        Specification<Problem> specification= (Specification<Problem>) (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate=criteriaBuilder.and();
            if(finalCheckVisible)
                predicate=criteriaBuilder.and(predicate,criteriaBuilder.equal(root.get("visible"),true));
            if(keyword!=null&&!"".equals(keyword.trim())) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.or(criteriaBuilder.like(root.get("title"), "%" + keyword.trim() + "%"),
                                criteriaBuilder.like(root.get("name"), "%" + keyword.trim() + "%")));
            }
            if(tags!=null&&!tags.isEmpty()){
                //TODO 考虑转义，使用更安全的转换方式
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                for (int i=0;i<tags.size();i++) {
                    if(i>0)
                        sb.append(',');
                    sb.append('"').append(tags.get(i)).append('"');
                }
                sb.append("]");
                System.out.println(sb.toString());
                predicate=criteriaBuilder.and(predicate,criteriaBuilder.equal(
                        criteriaBuilder.function("JSON_CONTAINS",String.class,root.get("tag"),
                                criteriaBuilder.literal(sb.toString())),1));
            }
            return predicate;
        };
        PageRequest pageRequest=PageRequest.of(page,size, Sort.by(Sort.Order.asc("sortId"),Sort.Order.desc("id")));
        problems=problemRepository.findAll(specification,pageRequest);
        return responseBuilder.success(problems);
    }

    public responseBase<Problem> get(long id){
        Problem prob=problemRepository.findById(id).orElse(null);
        if(prob==null)
            return responseBuilder.fail(responseCode.PROBLEM_NOT_EXIST);
        return responseBuilder.success(prob);
    }

    public responseBase<String> changeVisible(long id) {
        Problem prob = problemRepository.findById(id).orElse(null);
        if (prob == null)
            return responseBuilder.fail(responseCode.PROBLEM_NOT_EXIST);
        prob.setVisible(!prob.getVisible());
        problemRepository.save(prob);
        return responseBuilder.success();
    }

    public responseBase<String> add(Problem problem) {
        problem.setAcceptedNumber(0);
        problem.setSubmissionNumber(0);
        if(problem.getSpj()==null)
            problem.setSpj(false);
        if(problem.getVisible()==null)
            problem.setVisible(false);
        problem.setCreateUser(new UserInfo(AuthUtil.getId()));
        problem.setCreateTime(new Timestamp(new Date().getTime()));
        problem.setUpdateUser(problem.getCreateUser());
        problem.setUpdateTime(problem.getCreateTime());
        problemRepository.save(problem);
        return responseBuilder.success(problem.getId().toString());
    }

    public responseBase<String> update(long id, Problem prob) {
        Problem data = problemRepository.findById(id).orElse(null);
        if (data == null)
            return responseBuilder.fail(responseCode.PROBLEM_NOT_EXIST);
        if(prob.getName()!=null)
            data.setName(prob.getName());
        if (prob.getSortId()!=null)
            data.setSortId(prob.getSortId());
        if (prob.getTitle() != null)
            data.setTitle(prob.getTitle());
        if (prob.getDescription() != null)
            data.setDescription(prob.getDescription());
        if (prob.getInputDescription() != null)
            data.setInputDescription(prob.getInputDescription());
        if (prob.getOutputDescription() != null)
            data.setOutputDescription(prob.getOutputDescription());
        if (prob.getHint() != null)
            data.setHint(prob.getHint());
        if (prob.getTag() != null)
            data.setTag(prob.getTag());
        if (prob.getSample() != null)
            data.setSample(prob.getSample());
        if (prob.getTestcaseMd5() != null)
            data.setTestcaseMd5(prob.getTestcaseMd5());
        if (prob.getAllowLanguage() != null)
            data.setAllowLanguage(prob.getAllowLanguage());
        if (prob.getTimeLimit()!=null && prob.getTimeLimit() > 0)
            data.setTimeLimit(prob.getTimeLimit());
        if (prob.getMemoryLimit()!=null && prob.getMemoryLimit() > 0)
            data.setMemoryLimit(prob.getMemoryLimit());
        if (prob.getSpj()) {
            data.setSpj(prob.getSpj());
            if (prob.getSpjCode() != null)
                data.setSpjCode(prob.getSpjCode());
            if (prob.getSpjLanguage() != null)
                data.setSpjLanguage(prob.getSpjLanguage());
            if (prob.getSpjMd5() != null)
                data.setSpjMd5(prob.getSpjMd5());
        }
        if(prob.getVisible()!=null)
            data.setVisible(prob.getVisible());
        data.setUpdateUser(new UserInfo(AuthUtil.getId()));
        data.setUpdateTime(new Timestamp(new Date().getTime()));
        problemRepository.save(data);
        return responseBuilder.success();
    }

    public responseBase<String> delete(long id) {
        if (!problemRepository.existsById(id))
            return responseBuilder.fail(responseCode.PROBLEM_NOT_EXIST);
        problemRepository.deleteById(id);
        return responseBuilder.success();
    }

    private void deleteDir(File file){
        if(file.isDirectory())
            for (File f : file.listFiles())
                deleteDir(f);
        file.delete();
    }
    private File multipartFileToFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        File f = new File(fileName);
        InputStream is = file.getInputStream();
        OutputStream os = new FileOutputStream(f);
        int bytesRead;
        byte[] buffer = new byte[8192];
        while ((bytesRead = is.read(buffer, 0, 8192)) != -1)
            os.write(buffer, 0, bytesRead);
        os.close();
        is.close();
        return f;
    }
    public responseBase uploadTestcase(MultipartFile multipartFile){
        String fileName = multipartFile.getOriginalFilename();
        if(!fileName.endsWith(".zip"))
            return responseBuilder.fail(responseCode.NOT_ZIP_EXTENSION);
        File f;
        ZipFile zipFile;
        try {
            f = multipartFileToFile(multipartFile);
            String dirMd5 = DigestUtils.md5DigestAsHex(fileName.getBytes());
            System.out.println(fileName + " " + dirMd5);
            File dir = new File("testcase"+File.separator+dirMd5);
            if (dir.exists())
                deleteDir(dir);
            dir.mkdir();
            Set<String> inName = new TreeSet<>(), outName = new TreeSet<>();
            zipFile = new ZipFile(f);
            Enumeration entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry e = (ZipEntry) entries.nextElement();
                if (e.isDirectory())
                    continue;
                File target = new File("testcase"+File.separator+dirMd5 + File.separator + e.getName());
                target.createNewFile();
                InputStream is = zipFile.getInputStream(e);
                OutputStream os = new FileOutputStream(target);
                ZipUtil.flow(is,os);
                os.close();
                is.close();
                String[] names = e.getName().split("\\.");
                if (names.length == 0)
                    continue;
                String suf = names[names.length - 1];
                String pref = e.getName().substring(0, e.getName().length() - suf.length() - 1);
                if (suf.equalsIgnoreCase("in"))
                    inName.add(pref);
                else if (suf.equalsIgnoreCase("out"))
                    outName.add(pref);
            }
            zipFile.close();
            f.delete();
            if (!inName.equals(outName))
                return responseBuilder.fail(responseCode.UNMATCHED_FILE);
            boolean allHasDigit = true;
            Map<BigInteger, String> ori = new TreeMap();
            for (String s : inName) {
                BigInteger t = new BigInteger("0");
                boolean hasDigit = false;
                for (Character c : s.toCharArray())
                    if (Character.isDigit(c)) {
                        t = t.multiply(new BigInteger("10")).add(new BigInteger(c.toString()));
                        hasDigit = true;
                    }
                if (!hasDigit || ori.containsKey(t)) {
                    allHasDigit = false;
                    break;
                }
                ori.put(t, s);
            }
            Map<String, Integer> order = new HashMap<>();
            if (allHasDigit) {
                int ord = 1;
                for (String s : ori.values()) {
                    order.put(s, ord);
                    ++ord;
                }
            } else {
                int ord = 1;
                for (String s : inName) {
                    order.put(s, ord);
                    ++ord;
                }
            }
            String testcaseMd5=new String();
            for (File src : dir.listFiles()){
                InputStream ins=new FileInputStream(src);
                testcaseMd5+=DigestUtils.md5DigestAsHex(ins);
                ins.close();
                String[] names = src.getName().split("\\.");
                String suf = names[names.length - 1];
                String pref = src.getName().substring(0, src.getName().length() - suf.length() - 1);
                if(suf.equalsIgnoreCase("in"))
                    src.renameTo(new File("testcase"+File.separator+dirMd5+File.separator+order.get(pref)+".in"));
                else
                    src.renameTo(new File("testcase"+File.separator+dirMd5+File.separator+order.get(pref)+".out"));
            }
            testcaseMd5=DigestUtils.md5DigestAsHex(testcaseMd5.getBytes());
            File targetDir=new File("testcase"+File.separator+testcaseMd5);
            if(targetDir.exists())
                deleteDir(targetDir);
            dir.renameTo(targetDir);
            System.out.println(testcaseMd5);
            System.out.println(dir);
//            File md5File=new File("testcase"+File.separator+"md5");
//            md5File.createNewFile();
//            new FileOutputStream(md5File).write(testcaseMd5.getBytes());

            File zip=new File("testcase"+File.separator+testcaseMd5+".zip");
            if(zip.exists())
                zip.delete();
            zip.createNewFile();
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zip)));
            InputStream ins;
            for(File file:targetDir.listFiles()) {
                ZipEntry zipEntry=new ZipEntry(file.getName());
                zos.putNextEntry(zipEntry);
                ins=new FileInputStream(file);
                ZipUtil.flow(ins,zos);
                ins.close();
            }
            zos.close();
            deleteDir(targetDir);
            if(!FTPUtil.upload(zip))
                return responseBuilder.fail(responseCode.FTP_UPLOAD_ERROR);
            return responseBuilder.success(testcaseMd5);
        } catch(IOException e) {
            return responseBuilder.fail(responseCode.READ_FILE_ERROR);
        }finally {

        }
    }
    public responseBase setTestcase(long id, MultipartFile multipartFile){
        Problem problem=problemRepository.findById(id).orElse(null);
        if(problem==null)
            return responseBuilder.fail(responseCode.PROBLEM_NOT_EXIST);
        responseBase resp=uploadTestcase(multipartFile);
        if(resp.getStatus()!=0)
            return resp;
        problem.setTestcaseMd5((String) resp.getData());
        problem.setUpdateUser(new UserInfo(AuthUtil.getId()));
        problem.setUpdateTime(new Timestamp(new Date().getTime()));
        problemRepository.save(problem);
        return responseBuilder.success(problem.getId());
    }

    @Deprecated
    public responseBase<List<String>> tags() {
        Set<String> set=new HashSet<>();
        for(Problem p:problemRepository.findAll())
            for(String tag:p.getTag())
                set.add(tag);
        return responseBuilder.success(new ArrayList<>(set));
    }
}
