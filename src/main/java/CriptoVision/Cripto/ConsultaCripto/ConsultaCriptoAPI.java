package CriptoVision.Cripto.ConsultaCripto;

import CriptoVision.Cripto.Repository.VariacaoRepository;
import CriptoVision.Cripto.domain.Moeda;
import CriptoVision.Cripto.domain.MoedaBinance;
import CriptoVision.Cripto.domain.Moedas;
import CriptoVision.Cripto.domain.Variacao;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.sound.sampled.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
        conteudo = conteudo.replace("<200,", ""); //todo isso aqui acontece, porque precisa pegar o response.getBody() e não o reponse direto
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
            if (moeda.getNome().equals("Solana")) {
                JSONObject objetoBD = data.getJSONObject(i);
                variacao.setNomeCripto(objetoBD.getString("name"));
                variacao.setPrecoBase(objetoBD.getJSONObject("quote").getJSONObject("USD").getBigDecimal("price"));

                //SELECT ultima moeda gravada
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
                BigDecimal valorAlerta = variacaoBD.getVariacaoBase().add(variacaoExternaAtual);

                variacao.setValorAlerta(valorAlerta);
                variacao.setVariacaoBase(variacaoExternaAtual);

                Date dataHoraAtual = new Date();

                if (valorAlerta.compareTo(new BigDecimal(2)) > 0) {
                    System.out.println("O valor é maior que 2%");
                    System.out.println("Hora: " + dataHoraAtual.getHours()+ ":" + dataHoraAtual.getMinutes());
                    alerta("compra");
                    System.out.println("COMPRE!!! => Variação: " + valorAlerta);
                } else if (valorAlerta.compareTo(new BigDecimal(-1)) < 0) {
                    System.out.println("O valor é menor que -1%");
                    System.out.println("Hora: " + dataHoraAtual.getHours()+ ":" + dataHoraAtual.getMinutes());
                    System.out.println("VENDA!!! => Variação: " + valorAlerta);
                    alerta("venda");
                } else {
                    System.out.println("O valor está entre +2% e -1%");
                    System.out.println("Hora: " + dataHoraAtual.getHours()+ ":" + dataHoraAtual.getMinutes());
                    System.out.println("MANTENHA!!! => Variação: " + valorAlerta);
                }

                repository.save(variacao);
            }
        }
        moedas.setMoedas(listaMoedas);
        return moedas.getMoedas();

    }

    @GetMapping(value = "/listaCriptosBinance")
    public String getListaCriptosBinance() {
        // A ideia é varrer toda alista e depois trocar pelo nome, Contudo irá retornar uma só Moeda
        String url = "https://api1.binance.com/api/v3/ticker/24hr";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        String resposta = response.getBody(); //todo surround o método
        JSONArray respostaJson = new JSONArray(resposta);
        MoedaBinance moedaBinance = new MoedaBinance();

        for (int i = 0; i < respostaJson.length(); i++){
            JSONObject respostaAux = respostaJson.getJSONObject(i);
            //todo só falta adicionar ao banco, fazer a rotina de comparação e adicionar o alerta sonoro
            if(respostaAux.getString("symbol").equals("SOLUSDT")){
                moedaBinance.setNomeMoeda(respostaAux.getString("symbol"));
                moedaBinance.setPercentual24h(respostaAux.getBigDecimal("priceChangePercent"));
                moedaBinance.setValor(respostaAux.getBigDecimal("lastPrice")); //todo confirmar
            }
        }

        return resposta;
}

    private String alerta(String alerta) { //todo Ver uma forma de usar o som direto da pasta do projeto sem precisar do caminho inteiro
        try {
            URL oUrl = new URL("file:///C:/Users/Molina/Desktop/Desenvolvimento/Projeto_Cripto/CriptoVision/src/main/resources/sons/" + alerta + ".wav");
            Clip oClip = AudioSystem.getClip();
            AudioInputStream oStream = AudioSystem.getAudioInputStream(oUrl);
            oClip.open(oStream);
            oClip.loop(0);
        } catch (LineUnavailableException e) {
            return "";
        } catch (IOException e) { //todo tratar esses exceptions
            return "";
        } catch (UnsupportedAudioFileException e) {
            return "";
        }

        return "";
    }

    private Variacao buscaVariacaoBD() {
        return repository.findFirstByOrderByIdDesc();
    }

}
