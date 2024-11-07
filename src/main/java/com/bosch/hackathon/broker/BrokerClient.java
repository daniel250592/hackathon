package com.bosch.hackathon.broker;

import com.bosch.hackathon.models.PeopleCount;
import com.bosch.hackathon.repository.PeopleCountRepository;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Configuration
public class BrokerClient {

    private final PeopleCountRepository peopleCountRepository;

    public BrokerClient(PeopleCountRepository peopleCountRepository) {
        this.peopleCountRepository = peopleCountRepository;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{"tcp://84.247.187.255:1883"});
        options.setUserName("hack_user_2");
        options.setPassword("UEOBL2O_5y0bpp7".toCharArray());
        options.setCleanSession(false);
        options.setAutomaticReconnect(true);
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter("testClient", mqttClientFactory(),
                        "camera_2/nnvif-ej.RuleEngine.CountAggregation.OccupancyCounter.Occupancy 1");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setAutoStartup(true);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler(
    ) {
        return message -> {

            try {
                String payload = message.getPayload().toString();
                // Parse the payload to extract UtcTime and Data.Count
                JSONObject jsonObject = new JSONObject(payload);
                String utcTime = jsonObject.getString("UtcTime");
                String count = jsonObject.getJSONObject("Data").getString("Count");
                // Create a new JSON object with the required fields
                JSONObject newJsonObject = new JSONObject();
                newJsonObject.put("time", utcTime);
                newJsonObject.put("people", count);

                peopleCountRepository.save(new PeopleCount(Integer.valueOf(count), convertToLocalDateTime(utcTime)));

//                // Write the new JSON object to the file
//                Files.write(Paths.get("src/main/resources/result.txt"),
//                        ("," + System.lineSeparator() + newJsonObject.toString()).getBytes(),
//                        StandardOpenOption.APPEND);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
    }

    public static LocalDateTime convertToLocalDateTime(String utcString) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");

        return LocalDateTime.parse(utcString, formatter.withZone(ZoneOffset.UTC));

    }

}
