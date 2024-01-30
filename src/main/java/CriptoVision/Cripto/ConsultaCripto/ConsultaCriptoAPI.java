package CriptoVision.Cripto.ConsultaCripto;

import CriptoVision.Cripto.domain.Moeda;
import CriptoVision.Cripto.domain.Moedas;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("consulta-cripto")
public class ConsultaCriptoAPI {

    Logger log = LoggerFactory.getLogger(ConsultaCriptoAPI.class);

    @GetMapping(value = "/listaCriptos")
    public Moedas getListaCriptos(@RequestHeader(value = "X-CMC_PRO_API_KEY") String chaveCoinMkt ){
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

        Moedas moedas = new Moedas();
        List<Moeda> moedasTeste = new ArrayList<>();

        for(int i = 0; i < data.length(); i++){
            Moeda moeda = new Moeda();
            JSONObject objeto = data.getJSONObject(i);

            moeda.setNome(objeto.getString("name"));
            moeda.setPrecoAtual(objeto.getJSONObject("quote").getJSONObject("USD").getBigDecimal("price"));
            moeda.setUltimaHora(objeto.getJSONObject("quote").getJSONObject("USD").getBigDecimal("percent_change_1h"));
            moeda.setVinteQuatroHoras(objeto.getJSONObject("quote").getJSONObject("USD").getBigDecimal("percent_change_24h"));
            moeda.setSeteDias(objeto.getJSONObject("quote").getJSONObject("USD").getBigDecimal("percent_change_7d"));
            moeda.setTrintaDias(objeto.getJSONObject("quote").getJSONObject("USD").getBigDecimal("percent_change_30d"));

            moedasTeste.add(moeda);
        }
        moedas.setMoedas(moedasTeste);

        return moedas;
    }

}
