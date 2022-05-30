package catalog.web.controller;

import catalog.core.model.Preference;
import catalog.core.service.IPreferenceService;
import catalog.core.service.PreferenceService;
import catalog.web.converter.PreferenceConverter;
import catalog.web.dto.PreferenceDto;
import catalog.web.dto.PreferencesDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PreferenceController {
    public static final Logger log = LoggerFactory.getLogger(PreferenceController.class);

    @Autowired
    private IPreferenceService preferenceService;

    @Autowired
    private PreferenceConverter preferenceConverter;

//    @RequestMapping(value = "/preferences")
//    List<Preference> getAllPreferences() {
//        //todo: logs
//        return preferenceService.getAllPreferences();
//
//    }

    @RequestMapping(value = "/preferences")
    PreferencesDto getAllPreferences() {
        log.trace("getAllPreferences --- method entered");

    PreferencesDto p=new PreferencesDto(
            preferenceConverter.convertModelsToDtos(
                    preferenceService.getAllPreferences()));
        log.trace("getAllPreferences - method finished: result={}", p);
//        return new PreferencesDto(
//                preferenceConverter.convertModelsToDtos(
//                        preferenceService.getAllPreferences()));
    return p;
    }

    @RequestMapping(value = "/preferences/{filter}")
    PreferencesDto filterPreferences(@PathVariable String filter) {
        log.trace("filterPreferences --- method entered, type = {}",filter);
        PreferencesDto p=new PreferencesDto(
                preferenceConverter.convertModelsToDtos(
                        preferenceService.filterPreferencesByType(filter)));
        log.trace("filterPreferences - method finished: result={}", p);

        return p;
//        return new PreferencesDto(
//                preferenceConverter.convertModelsToDtos(
//                        preferenceService.filterPreferencesByType(filter)));
    }

    @RequestMapping(value = "/preferences/report/{id}")
    Double reportPreferences(@PathVariable Long id) {
        log.trace("reportPreferences --- method entered, id = {}",id);
        double res=preferenceService.getPercentageProduct_id(id);
        log.trace("reportPreferences - method finished: result={}", res);
        return res;
       // return  preferenceService.getPercentageProduct_id(id);
    }

    @RequestMapping(value = "/preferences", method = RequestMethod.POST)
    PreferenceDto addPreference(@RequestBody PreferenceDto preferenceDto) {
        log.trace("addPreference - method entered: preference={}", preferenceDto);
        Preference preference = preferenceConverter.convertDtoToModel(preferenceDto);

        Preference result = preferenceService.savePreference(preference);

        log.trace("addPreference - method finished: result ={}",result);
        return preferenceConverter.convertModelToDto(result);
    }

    @RequestMapping(value = "/preferences/{id}", method = RequestMethod.PUT)
    PreferenceDto updatePreference(@PathVariable Long id,
                                   @RequestBody PreferenceDto dto) {
        log.trace("updatePreference - method entered: preference={}", dto);
        Long getId=id;
        PreferenceDto p=preferenceConverter.convertModelToDto(
                preferenceService.updatePreference(
                        preferenceConverter.convertDtoToModel(dto)
                ));
        log.trace("updatePreference - method finished: result ={}",p);

        return p;
//        return
//                preferenceConverter.convertModelToDto(
//                        preferenceService.updatePreference(
//                                preferenceConverter.convertDtoToModel(dto)
//                        ));
    }

    @RequestMapping(value = "/preferences/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deletePreference(@PathVariable Long id) {
        log.trace("deletePreference - method entered: id={}", id);

        preferenceService.deletePreference(id);
        log.trace("deletePreference - method finished");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
