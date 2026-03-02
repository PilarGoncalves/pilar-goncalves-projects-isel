library ieee;
use ieee.std_logic_1164.all;

entity SerialReceiver_SRC_tb is
end SerialReceiver_SRC_tb;

architecture behavioral of SerialReceiver_SRC_tb is

component SerialReceiver_SRC
	port (
        SS : in std_logic;
        MCLK : in std_logic;
        SDX : in std_logic;
        RESET : in std_logic;
        SCLK : in std_logic;
        accept : in std_logic;
        D : out std_logic_vector(7 downto 0);
        DXval : out std_logic
        );
end component;


constant MCLK_PERIOD       : time := 20 ns;
constant MCLK_HALF_PERIOD  : time := MCLK_PERIOD / 2;


signal SDX_tb     : std_logic := '0';
signal SCLK_tb    : std_logic := '0';
signal SS_tb      : std_logic := '1';  
signal accept_tb  : std_logic := '0';
signal Reset_tb   : std_logic := '0';
signal MCLK_tb    : std_logic := '0';
signal D_tb       : std_logic_vector(7 downto 0);
signal DXval_tb   : std_logic;

begin

UUT: SerialReceiver_SRC
	port map(
        SS => SS_tb,
        MCLK => MCLK_tb,
        SDX => SDX_tb,
        RESET => RESET_tb,
        SCLK => SCLK_tb,
        accept => accept_tb,
        D => D_tb,
        DXval => DXval_tb
        );


clk_gen : process
    begin
            MCLK_tb <= '0';
            wait for MCLK_HALF_PERIOD;
            MCLK_tb <= '1';
            wait for MCLK_HALF_PERIOD;
    end process;


    stimulus : process
    begin
	RESET_tb <= '1';
        wait for MCLK_PERIOD;
        RESET_tb <= '0';
        wait for MCLK_PERIOD;

--Start
        SS_tb <= '0';         
        accept_tb <= '1';     
        wait for MCLK_PERIOD;
        accept_tb <= '0';
        wait for MCLK_PERIOD;


-- bit 0
        SDX_tb <= '0';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '1';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '0';
        wait for MCLK_PERIOD * 5;

-- bit 1
        SDX_tb <= '1';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '1';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '0';
        wait for MCLK_PERIOD * 5;

-- bit 2
        SDX_tb <= '0';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '1';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '0';
        wait for MCLK_PERIOD * 5;

-- bit 3
        SDX_tb <= '1';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '1';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '0';
        wait for MCLK_PERIOD * 5;

-- bit 4
        SDX_tb <= '0';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '1';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '0';
        wait for MCLK_PERIOD * 5;

-- bit 5
        SDX_tb <= '1';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '1';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '0';
        wait for MCLK_PERIOD * 5;

-- bit 6
        SDX_tb <= '0';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '1';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '0';
        wait for MCLK_PERIOD * 5;

-- bit 7
        SDX_tb <= '1';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '1';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '0';
        wait for MCLK_PERIOD * 5;

-- bit 8 (P)
        SDX_tb <= '0';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '1';
        wait for MCLK_PERIOD * 5;
        SCLK_tb <= '0';
        wait for MCLK_PERIOD * 5;

        SCLK_tb <= '1'; 
	wait for MCLK_PERIOD * 5;
	SCLK_tb <= '0';
	wait for MCLK_PERIOD * 5;


        SS_tb <= '1';  
        wait for MCLK_PERIOD * 5;

        wait;
    end process;

end behavioral;
