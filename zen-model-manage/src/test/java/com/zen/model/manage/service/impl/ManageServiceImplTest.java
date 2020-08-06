package com.zen.model.manage.service.impl;

import com.zen.model.manage.bean.Feature;
import com.zen.model.manage.dto.ModelInfoDto;
import com.zen.model.manage.service.ManageService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: morris
 * @Date: 2020/6/12 9:01
 * @description
 * @reviewer
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class ManageServiceImplTest {
    @Autowired
    private ManageService manageService;

    @Test
    void createModel() {
        ModelInfoDto modelInfoDto = new ModelInfoDto();
        Feature feature = new Feature("gender","int","25");
        Feature feature1 = new Feature("gender","int","26");
        Feature feature2 = new Feature("region","string","深圳");
        ArrayList<Feature> features = new ArrayList<>();
        features.add(feature);
        features.add(feature1);
        features.add(feature2);
        modelInfoDto.setHdfsPath("hdfs://master:9000/input/article1234");
        modelInfoDto.setModelType("mLeap");
        modelInfoDto.setWarmUpFeature(features);
        manageService.createModel(modelInfoDto);
    }

    @Test
    void updateModel() throws ValidationException {
        ModelInfoDto modelInfoDto = new ModelInfoDto();
        Feature feature = new Feature("gender","int","25");
        Feature feature1 = new Feature("gender","int","26");
        Feature feature2 = new Feature("region","string","深圳");
        ArrayList<Feature> features = new ArrayList<>();
        features.add(feature);
        features.add(feature1);
        features.add(feature2);
        modelInfoDto.setToken("602874adfe37587c");
        modelInfoDto.setHdfsPath("hdfs://master:9000/input/article");
        modelInfoDto.setModelType("mLeap");
        modelInfoDto.setWarmUpFeature(features);
        System.out.println(manageService.updateModel(modelInfoDto));
    }

    @Test
    void offlineModel() {
        System.out.printf(manageService.offlineModel("602874adfe37587c"));
    }

    @Test
    void rollbackModel() {
        manageService.rollbackModel("602874adfe37587c");
    }

    @Test
    void deleteModel() {
        System.out.println(manageService.deleteModel("602874adfe37587c", 2));
    }
}