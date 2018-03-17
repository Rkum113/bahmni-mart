package org.bahmni.mart.helper;

import org.apache.commons.io.FileUtils;
import org.bahmni.mart.AbstractBaseBatchIT;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.bahmni.mart.CommonTestHelper.setValuesForMemberFields;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SeparateTableConfigHelperIT extends AbstractBaseBatchIT {

    @Autowired
    private SeparateTableConfigHelper separateTableConfigHelper;

    private String emptyDefaultConfPath = "src/test/resources/conf/emptyDefaultConfig.json";
    private String emptyImplConfPath = "src/test/resources/conf/emptyImplConfig.json";

    @Override
    @After
    public void tearDown() throws Exception {
        FileUtils.deleteQuietly(new File(emptyDefaultConfPath));
        FileUtils.deleteQuietly(new File(emptyImplConfPath));
    }

    @Test
    public void shouldReturnListOfMultiSelectAndAddMore() {
        List<String> conceptNames = separateTableConfigHelper.getAddMoreAndMultiSelectConceptNames();
        assertEquals(3, conceptNames.size());
        List<String> expected = Arrays.asList("FSTG, Specialty determined by MLO", "OR, Operation performed", "Video");
        assertThat(conceptNames, containsInAnyOrder(expected.toArray()));
    }

    @Test
    public void shouldReturnConceptNamesListWhenImplConfigFileIsNotPresentInTheGivenPath() throws Exception {
        SeparateTableConfigHelper separateTableConfigHelper = new SeparateTableConfigHelper();
        setValuesForMemberFields(separateTableConfigHelper, "implementationConfigFile", "/src/notPresent.json");
        setValuesForMemberFields(separateTableConfigHelper, "defaultConfigFile",
                "src/test/resources/conf/defaultApp.json");
        setValuesForMemberFields(separateTableConfigHelper, "ignoreConcepts", "");

        List<String> conceptNames = separateTableConfigHelper.getAddMoreAndMultiSelectConceptNames();
        assertEquals(2, conceptNames.size());
        List<String> expected = Arrays.asList("OR, Operation performed", "Video");
        assertThat(conceptNames, containsInAnyOrder(expected.toArray()));
    }

    @Test
    public void shouldReturnConceptNamesListWhenDefaultConfigFileIsNotPresentInTheGivenPath() throws Exception {
        SeparateTableConfigHelper separateTableConfigHelper = new SeparateTableConfigHelper();
        setValuesForMemberFields(separateTableConfigHelper, "ignoreConcepts", "");
        setValuesForMemberFields(separateTableConfigHelper, "implementationConfigFile",
                "src/test/resources/conf/implementationApp.json");
        setValuesForMemberFields(separateTableConfigHelper, "defaultConfigFile", "/src/notPresent.json");

        List<String> conceptNames = separateTableConfigHelper.getAddMoreAndMultiSelectConceptNames();
        assertEquals(2, conceptNames.size());
        List<String> expected = Arrays.asList("FSTG, Specialty determined by MLO", "MH, Name of MLO");
        assertThat(conceptNames, containsInAnyOrder(expected.toArray()));
    }

    @Test
    public void shouldGiveEmptyListWhenBothConfigFilesAreEmpty() throws Exception {
        FileUtils.touch(new File(emptyDefaultConfPath));
        FileUtils.touch(new File(emptyImplConfPath));

        SeparateTableConfigHelper separateTableConfigHelper = new SeparateTableConfigHelper();
        setValuesForMemberFields(separateTableConfigHelper, "ignoreConcepts", "");
        setValuesForMemberFields(separateTableConfigHelper, "implementationConfigFile", emptyDefaultConfPath);
        setValuesForMemberFields(separateTableConfigHelper, "defaultConfigFile", emptyImplConfPath);

        assertTrue(separateTableConfigHelper.getAddMoreAndMultiSelectConceptNames().isEmpty());
    }
}