library ieee;
use ieee.std_logic_1164.all;

entity SerialControl_tb is
end SerialControl_tb;

architecture behavioral of SerialControl_tb is

component SerialControl is
	port( 
	RESET : in std_logic;
	CLK : in std_logic;
	enRx : in std_logic;
	accept : in std_logic;
   pFlag : in std_logic;
   dFlag : in std_logic;
	RXerror : in std_logic;
   wr : out std_logic;
	init : out std_logic;
	DXval : out std_logic
	);
end component;

constant MCLK_PERIOD : time := 20 ns;
constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

signal RESET_tb : std_logic := '0';
signal CLK_tb : std_logic := '0';
signal enRx_tb : std_logic := '0';
signal accept_tb : std_logic := '0';
signal pFlag_tb : std_logic := '0';
signal dFlag_tb : std_logic := '0';
signal RXerror_tb : std_logic := '0';
signal wr_tb : std_logic;
signal init_tb : std_logic;
signal DXval_tb : std_logic;

begin

UUT: SerialControl
        port map (
				RESET => RESET_tb,
				CLK => CLK_tb,
				enRx => enRx_tb,
				accept => accept_tb,
				pFlag => pFlag_tb,
				dFlag => dFlag_tb,
				RXerror => RXerror_tb,
				wr => wr_tb,
				init => init_tb,
				DXval => DXval_tb);
		  
		  
clk_gen : process 
begin 
		CLK_tb <= '0';
		wait for MCLK_HALF_PERIOD;
		CLK_tb <= '1';
		wait for MCLK_HALF_PERIOD;
end process;

   stimulus : process
   begin
	
        RESET_tb <= '1';
        wait for MCLK_PERIOD;
        RESET_tb <= '0';
        wait for MCLK_PERIOD;

        -- Ativa a receção (vai para RECEIVING_STATE)
        enRx_tb <= '1';
        wait for MCLK_PERIOD;

        -- Simula o fim da receção de dados (vai para PARITYCHECK_STATE)
        dFlag_tb <= '1';
        wait for MCLK_PERIOD;

        -- Simula o fim da paridade correta (vai para SENT_STATE)
        pFlag_tb <= '1';
        RXerror_tb <= '0';
        enRx_tb <= '0';
        wait for MCLK_PERIOD;

        -- Simula a aceitação de dados (vai para WAITACCEPT_STATE)
        accept_tb <= '1';
        wait for MCLK_PERIOD;

        -- Termina a aceitação (volta ao estado inicial)
        accept_tb <= '0';
        wait for MCLK_PERIOD;

		  
		  
		  RESET_tb <= '1';
		  wait for MCLK_PERIOD;
	     RESET_tb <= '0';
	     wait for MCLK_PERIOD;

	     enRX_tb <= '1';
	     wait for MCLK_PERIOD;

	     dFlag_tb <= '1';
	     wait for MCLK_PERIOD;

	     pFlag_tb <= '1';
	     RXerror_tb <= '1';  -- Erro de paridade
	     enRX_tb <= '0';
	     wait for MCLK_PERIOD;

	     -- Não deve avançar para SENT_STATE nem ativar DXval
	     accept_tb <= '1';
	     wait for MCLK_PERIOD;

	     accept_tb <= '0';
	     wait for MCLK_PERIOD;
		  
        
        wait;
	end process;

end behavioral;