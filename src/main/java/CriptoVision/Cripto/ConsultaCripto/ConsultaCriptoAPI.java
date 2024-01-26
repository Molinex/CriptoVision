package CriptoVision.Cripto.ConsultaCripto;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.jsoup.Jsoup;

import java.math.BigDecimal;

@RestController
@RequestMapping("consulta-cripto")
public class ConsultaCriptoAPI {

    Logger log = LoggerFactory.getLogger(ConsultaCriptoAPI.class);

    @GetMapping(value = "/listaCriptos")
    public String getListaCriptos(@RequestHeader(value = "X-CMC_PRO_API_KEY") String chaveCoinMkt ){
        log.info("Entrada no EP de Lista de Criptos");

        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", chaveCoinMkt);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        String conteudo = response.toString();
        conteudo = conteudo.replace("<200,","");
        conteudo = conteudo.replace(">","");

        // Converte o conteúdo em JSON
        JSONObject json = new JSONObject(conteudo);

        JSONArray data = json.getJSONArray("data");
        JSONObject primeiroObjeto = data.getJSONObject(0);

        String moeda = primeiroObjeto.getString("name");
        BigDecimal volume24h = primeiroObjeto.getJSONObject("quote").getJSONObject("USD").getBigDecimal("volume_24h");
        BigDecimal percent1h = primeiroObjeto.getJSONObject("quote").getJSONObject("USD").getBigDecimal("percent_change_1h");;
        System.out.println("Valores:\nMoeda: " + moeda + "\nVolume 24h: " + volume24h + "\nPercentual 1h: " + percent1h);

//        return new ResponseEntity(response.getBody(), HttpStatus.OK);
        return json.toString();
    }

}
