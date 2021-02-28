# NextCash

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e514adb7e1414702b539e5c46018dc12)](https://app.codacy.com/gh/NextPlugins/NextCash?utm_source=github.com&utm_medium=referral&utm_content=NextPlugins/NextCash&utm_campaign=Badge_Grade)

Um sistema simples e completo de uma economia secundária ("cash") para servidores de Minecraft, quase que 100% configurável, com pódio em chat/menu e NPCs, suporte ao PlaceholderAPI, informações salvas em banco de dados SQL e com uma robusta [API](https://github.com/NextPlugins/NextCash/tree/main/src/main/java/com/nextplugins/cash/api) (em breve uma Wiki) para desenvolvedores. [Prints in-game](https://imgur.com/gallery/QAf28xj).

## Comandos
|Comando         |Descrição                      |Permissão                    |
|----------------|-------------------------------|-----------------------------|
|/cash           |Veja a sua, ou a quantia de cash de outra pessoa.|Nenhuma    |
|/cash enviar    |Envie uma quantia de cash para outra pessoa.|`nextcash.command.pay`|
|/cash top       |Veja os jogadores com as maiores fortunas do servidor.|`nextcash.command.top`|
|/cash ajuda     |Veja os comandos disponíveis do sistema.|`nextcash.command.help`|
|/cash add       |Adicione uma quantia de cash para alguém.|`nextcash.command.add`|
|/cash set       |Altere a quantia de cash de alguém.|`nextcash.command.set`|
|/cash remove    |Remova uma quantia de cash de alguém.|`nextcash.command.remove`|
|/cash reset     |Zere o saldo de cash de algum jogador.|`nextcash.command.reset`|
|/cash npc       |Veja a ajuda para o sistema de NPCs.|`nextcash.command.npc.help`|
|/cash npc add   |Adicione uma localização de spawn de NPC.|`nextcash.command.npc.add`|
|/cash npc remove|Remova uma localização de spawn de NPC.|`nextcash.command.npc.remove`|

## Download

Você pode encontrar o plugin pronto para baixar [**aqui**](https://github.com/NextPlugins/NextCash/releases), ou se você quiser, pode optar por clonar o repositório e dar build no plugin com suas alterações.

## Configuração

O plugin conta com vários arquivos de configuração, em que se pode configurar quase tudo que você quiser.

Placeholder: "{placeholderapi_nextcash_amount}"

## Dependências
O NextCash necessita do [Citizens](https://dev.bukkit.org/projects/citizens) para o sistema de NPCs, [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) para suporte a placeholders, [HolographicDisplays](https://dev.bukkit.org/projects/holographic-displays) para hologramas (utilizado no sistema de ranking por NPC). As dependências de desenvolvimento serão baixadas automaticamente quando o plugin for habilitado pela primeira vez.

### Tecnologias usadas
- [PDM](https://github.com/knightzmc/pdm) - Faz o download de dependências de desenvolvimento durante o carregamento do servidor.
- [Lombok](https://projectlombok.org/) - Gera “getters”, “setters” e outros métodos uteis durante a compilação através de anotações.

**APIs e Frameworks**

- [command-framework](https://github.com/SaiintBrisson/command-framework) - Framework para criação e gerenciamento de comandos.
- [inventory-api](https://github.com/HenryFabio/inventory-api) - API para criação e o gerenciamento de inventários customizados.
- [sql-provider](https://github.com/henryfabio/sql-provider) - Provê a conexão com o banco de dados.
