package ru.otus.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.model.Client;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DbServiceClientCachedTest {

    @Mock
    private DBServiceClient delegate;

    private DBServiceClient cachedService;

    @BeforeEach
    void setUp() {
        cachedService = new DbServiceClientCachedImpl(delegate);
    }

    @Test
    @DisplayName("should return cached result on repeated get")
    void cacheReturnsSameResultOnRepeatedGet() {
        when(delegate.getClient(1L)).thenReturn(Optional.of(new Client(1L, "cached-client")));

        var result1 = cachedService.getClient(1L);
        var result2 = cachedService.getClient(1L);

        assertThat(result1).isPresent().get().extracting(Client::getName).isEqualTo("cached-client");
        assertThat(result2).isPresent();
        verify(delegate, times(1)).getClient(1L);
    }

    @Test
    @DisplayName("should store result in cache after save")
    void cacheStoresResultAfterSave() {
        when(delegate.saveClient(any())).thenReturn(new Client(1L, "saved"));

        var saved = cachedService.saveClient(new Client("saved"));
        var found = cachedService.getClient(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("saved");
        verify(delegate, never()).getClient(anyLong());
    }

    @Test
    @DisplayName("should not cache findAll results")
    void findAllBypassesCache() {
        when(delegate.findAll()).thenReturn(List.of(new Client(1L, "first")));

        cachedService.findAll();
        cachedService.findAll();

        verify(delegate, times(2)).findAll();
    }
}
