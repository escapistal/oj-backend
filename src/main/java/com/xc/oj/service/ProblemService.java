package com.xc.oj.service;

import com.xc.oj.entity.Problem;
import com.xc.oj.repository.ProblemRepository;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import com.xc.oj.util.FTPUtil;
import com.xc.oj.util.ZipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

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

    public responseBase<List<Problem>> listAll() {
        return responseBuilder.success(problemRepository.findAll());
    }

    public responseBase<List<Problem>> list() {
        return responseBuilder.success(problemRepository.findByVisibleTrue());
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
        problem.setCreateTime(new Timestamp(new Date().getTime()));
        problem.setAcceptedNumber(0);
        problem.setSubmissionNumber(0);
        if(problem.getSpj()==null)
            problem.setSpj(false);
        if(problem.getVisible()==null)
            problem.setVisible(false);
        problemRepository.save(problem);
        return responseBuilder.success();
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

        if (prob.getSample() != null)
            data.setSample(prob.getSample());
        if (prob.getTestCaseMd5() != null)
            data.setTestCaseMd5(prob.getTestCaseMd5());
        if (prob.getAllowLanguage() != null)
            data.setAllowLanguage(prob.getAllowLanguage());
        if (prob.getTimeLimit()!=null && prob.getTimeLimit() > 0)
            data.setTimeLimit(prob.getTimeLimit());
        if (prob.getMemoryLimit()!=null && prob.getMemoryLimit() > 0)
            data.setMemoryLimit(prob.getMemoryLimit());
        if(prob.getSpj()) {
            data.setSpj(prob.getSpj());
            if (prob.getSpjCode() != null)
                data.setSpjCode(prob.getSpjCode());
            if (prob.getSpjLanguage() != null)
                data.setSpjLanguage(prob.getSpjLanguage());
            if (prob.getSpjMd5() != null)
                data.setSpjMd5(prob.getSpjMd5());
        }
        if(prob.getVisible())
            data.setVisible(prob.getVisible());
        data.setUpdateTime(new Timestamp(new Date().getTime()));
        problemRepository.save(data);
        return responseBuilder.success();
    }

    public responseBase<String> delete(long id) {
        Problem prob = problemRepository.findById(id).orElse(null);
        if (prob == null)
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
    public responseBase setTestcase(long id, MultipartFile multipartFile){
        Problem problem=problemRepository.findById(id).orElse(null);
        if(problem==null)
            return responseBuilder.fail(responseCode.PROBLEM_NOT_EXIST);
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
            problem.setTestCaseMd5(testcaseMd5);
            problemRepository.save(problem);
            if(!FTPUtil.upload(zip))
                return responseBuilder.fail(responseCode.FTP_UPLOAD_ERROR);
            return responseBuilder.success(inName.size());
        } catch(IOException e)
        {
            return responseBuilder.fail(responseCode.READ_FILE_ERROR);
        }finally {

        }

    }
}
