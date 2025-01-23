package kz.hustle.equeue.service;

import kz.hustle.equeue.config.TTSConfig;
import kz.hustle.equeue.repository.TTSSettingsRepository;
import kz.hustle.equeue.service.tts.TTSProvider;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class TTSBeanManager {

    private final ConfigurableApplicationContext context;

    public TTSBeanManager(ConfigurableApplicationContext context) {
        this.context = context;
    }

    public synchronized void recreateTtsProviderBean() {
        try {
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();

            if (beanFactory.containsSingleton("tts")) {
                beanFactory.destroySingleton("tts");
            }

            TTSProvider newProvider = context.getBean(TTSConfig.class).tts(
                    context.getBean(TTSSettingsRepository.class)
            );

            System.out.println("TTS Provider bean recreated successfully.");
        } catch (Exception e) {
            System.err.println("Failed to recreate TTS Provider bean: " + e.getMessage());
            e.printStackTrace();
        }
    }
}