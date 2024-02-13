package CriptoVision.Cripto.ConsultaCripto;

import CriptoVision.Cripto.Repository.VariacaoRepository;
import CriptoVision.Cripto.domain.Moeda;
import CriptoVision.Cripto.domain.Moedas;
import CriptoVision.Cripto.domain.Variacao;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("consulta-cripto")
public class ConsultaCriptoAPI {

    Logger log = LoggerFactory.getLogger(ConsultaCriptoAPI.class);

    @Autowired
    private VariacaoRepository repository;

    @GetMapping(value = "/listaCriptos")
    public Moedas getListaCriptos(@RequestHeader(value = "X-CMC_PRO_API_KEY") String chaveCoinMkt) {
        log.info("Entrada no EP de Lista de Criptos");

        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", chaveCoinMkt);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        String conteudo = response.toString();
        conteudo = conteudo.replace("<200,", "");
        conteudo = conteudo.replace(">", "");

        // Converte o conteúdo em JSON
        JSONObject json = new JSONObject(conteudo);
        JSONArray data = json.getJSONArray("data");

        Moedas moedas = new Moedas();
        List<Moeda> moedasTeste = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
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

    @GetMapping(value = "/listaCriptosP")
    public List<Moeda> getListaCriptosP(@RequestParam(value = "chaveApi") String chaveCoinMkt) {

        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", chaveCoinMkt);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        Variacao variacao = new Variacao();

        String conteudo = response.toString();
        conteudo = conteudo.replace("<200,", "");
        conteudo = conteudo.replace(">", "");

        // Converte o conteúdo em JSON
        JSONObject json = new JSONObject(conteudo);
        JSONArray data = json.getJSONArray("data");

        Moedas moedas = new Moedas();
        List<Moeda> listaMoedas = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            Moeda moeda = new Moeda();
            JSONObject objeto = data.getJSONObject(i);

            moeda.setNome(objeto.getString("name"));
            moeda.setPrecoAtual(objeto.getJSONObject("quote").getJSONObject("USD").getBigDecimal("price"));
            moeda.setUltimaHora(objeto.getJSONObject("quote").getJSONObject("USD").getBigDecimal("percent_change_1h"));
            moeda.setVinteQuatroHoras(objeto.getJSONObject("quote").getJSONObject("USD").getBigDecimal("percent_change_24h"));
            moeda.setSeteDias(objeto.getJSONObject("quote").getJSONObject("USD").getBigDecimal("percent_change_7d"));
            moeda.setTrintaDias(objeto.getJSONObject("quote").getJSONObject("USD").getBigDecimal("percent_change_30d"));

            listaMoedas.add(moeda);

            //Gravação no BD - BTC
            if (i == 0) {
                JSONObject objetoBD = data.getJSONObject(i);
                variacao.setNomeCripto(objetoBD.getString("name"));
                variacao.setPrecoBase(objetoBD.getJSONObject("quote").getJSONObject("USD").getBigDecimal("price"));

                //SELECT ultimo BTC gravado
                Variacao variacaoBD = buscaVariacaoBD();

                if (variacaoBD == null) {
                    variacao.setValorAlerta(new BigDecimal(0.0));
                    variacao.setVariacaoBase(new BigDecimal(0.0));
                    repository.save(variacao);
                    return moedas.getMoedas();
                }

                BigDecimal precoAPI = objetoBD.getJSONObject("quote").getJSONObject("USD").getBigDecimal("price");
                BigDecimal precoBase = variacaoBD.getPrecoBase();
                BigDecimal variacaoExternaAtual = precoAPI.subtract(precoBase).divide(precoBase, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));

                variacao.setVariacaoBase(variacaoExternaAtual);
                variacao.setValorAlerta(variacaoBD.getValorAlerta());

                repository.save(variacao);
            }
        }
        moedas.setMoedas(listaMoedas);
        return moedas.getMoedas();


        // ----
//        Moedas moedas = new Moedas();
//        moedas = preencheMoedasFake(moedas);
//        Variacao variacaoBD = buscaVariacaoBD();
//        System.out.println(variacaoBD.toString());
//        return moedas.getMoedas();
        //-----

    }

    private Variacao buscaVariacaoBD() {
        return repository.findFirstByOrderByIdDesc();
    }

    private Moedas preencheMoedasFake(Moedas moedasParam) {
        Moedas listaMoedas = new Moedas();

        List<Moeda> moedas = new ArrayList<>();

        Moeda moeda1 = new Moeda();
        moeda1.setNome("BitFake");
        moeda1.setUltimaHora(BigDecimal.valueOf(-0.19475947));
        moeda1.setVinteQuatroHoras(BigDecimal.valueOf(4.63687383));
        moeda1.setSeteDias(BigDecimal.valueOf(10.48645335));
        moeda1.setTrintaDias(BigDecimal.valueOf(3.61039286));
        moeda1.setPrecoAtual(BigDecimal.valueOf(47501.00551688974));
        moedas.add(moeda1);

        Moeda moeda2 = new Moeda();
        moeda2.setNome("BNBFake");
        moeda2.setUltimaHora(BigDecimal.valueOf(0.33804611));
        moeda2.setVinteQuatroHoras(BigDecimal.valueOf(2.96765624));
        moeda2.setSeteDias(BigDecimal.valueOf(8.95617728));
        moeda2.setTrintaDias(BigDecimal.valueOf(-0.62560553));
        moeda2.setPrecoAtual(BigDecimal.valueOf(2501.9794796899523));
        moedas.add(moeda2);

        listaMoedas.setMoedas(moedas);

        return listaMoedas;
    }

}
