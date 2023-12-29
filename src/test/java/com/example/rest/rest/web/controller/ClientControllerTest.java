package com.example.rest.rest.web.controller;

import com.example.rest.rest.AbstractTestController;
import com.example.rest.rest.StringTestUtils;
import com.example.rest.rest.mapper.v1.ClientMapper;
import com.example.rest.rest.model.Client;
import com.example.rest.rest.model.Order;
import com.example.rest.rest.service.ClientService;
import com.example.rest.rest.web.model.ClientListResponse;
import com.example.rest.rest.web.model.ClientResponse;
import com.example.rest.rest.web.model.OrderResponse;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

public class ClientControllerTest extends AbstractTestController {
    @MockBean
    private ClientService clientService;

    @MockBean
    private ClientMapper clientMapper;

    @Test
    public void whenFindAll_thenReturnAllClients() throws Exception {
        List<Client> clients = new ArrayList<>();
        clients.add(createClient(1L, null));
        Order order = createOrder(1L, 100L, null);
        clients.add(createClient(2L, order));

        List<ClientResponse> clientResponses = new ArrayList<>();
        clientResponses.add(createClientResponse(1L, null));
        OrderResponse orderResponse = createOrderResponse(1L, 100L);

        clientResponses.add(createClientResponse(2L, orderResponse));

        ClientListResponse clientListResponse = new ClientListResponse(clientResponses);

        Mockito.when(clientService.findAll()).thenReturn(clients);

        Mockito.when(clientMapper.clientListToClientResponseList(clients)).thenReturn(clientListResponse);

        String actualResponse = mockMvc.perform(get("/api/v1/client"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = StringTestUtils.readStringFromResource("response/find_all_clients_response.json");

        Mockito.verify(clientService, Mockito.times(1)).findAll();
        Mockito.verify(clientMapper, Mockito.times(1)).clientListToClientResponseList(clients);

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }
}
