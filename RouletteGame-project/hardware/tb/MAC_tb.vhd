library ieee;
use ieee.std_logic_1164.all;

entity MAC_tb is
end MAC_tb;

architecture behavioral of MAC_tb is

    component MAC
        port(
            putGet : in std_logic;
            incPut : in std_logic;
            incGet : in std_logic;
            CLK    : in std_logic;
            RESET  : in std_logic;
            full   : out std_logic;
            empty  : out std_logic;
            A      : out std_logic_vector(3 downto 0)
        );
    end component;

    constant MCLK_PERIOD      : time := 20 ns;
    constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

    signal putGet_tb : std_logic := '0';
    signal incPut_tb : std_logic := '0';
    signal incGet_tb : std_logic := '0';
    signal RESET_tb  : std_logic := '0';
    signal CLK_tb    : std_logic := '0';
    signal full_tb   : std_logic;
    signal empty_tb  : std_logic;
    signal A_tb      : std_logic_vector(3 downto 0);

begin

    UUT: MAC port map(
        putGet => putGet_tb,
        incPut => incPut_tb,
        incGet => incGet_tb,
        CLK    => CLK_tb,
        RESET  => RESET_tb,
        full   => full_tb,
        empty  => empty_tb,
        A      => A_tb
    );

    -- Clock sem loop
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

        -- INITIAL_STATE: putGet = 0 (put)
        putGet_tb <= '0';

        -- incPut
        incPut_tb <= '1';
        wait for MCLK_PERIOD;
        incPut_tb <= '0';
        wait for MCLK_PERIOD;

        incPut_tb <= '1';
        wait for MCLK_PERIOD;
        incPut_tb <= '0';
        wait for MCLK_PERIOD;

        incPut_tb <= '1';
        wait for MCLK_PERIOD;
        incPut_tb <= '0';
        wait for MCLK_PERIOD;

        -- passa a get
        putGet_tb <= '1';

        -- incGet
        incGet_tb <= '1';
        wait for MCLK_PERIOD;
        incGet_tb <= '0';
        wait for MCLK_PERIOD;

        incGet_tb <= '1';
        wait for MCLK_PERIOD;
        incGet_tb <= '0';
        wait for MCLK_PERIOD;

        incGet_tb <= '1';
        wait for MCLK_PERIOD;
        incGet_tb <= '0';
        wait for MCLK_PERIOD;

        -- passa a put outra vez
        putGet_tb <= '0';

        -- mais puts até full = 1

        incPut_tb <= '1'; 
	wait for MCLK_PERIOD; 
	incPut_tb <= '0'; 
	wait for MCLK_PERIOD;
        
        incPut_tb <= '1'; 
	wait for MCLK_PERIOD; 
	incPut_tb <= '0'; 
	wait for MCLK_PERIOD;
        
        incPut_tb <= '1'; 
	wait for MCLK_PERIOD; 
	incPut_tb <= '0'; 
	wait for MCLK_PERIOD;
        
        incPut_tb <= '1'; 
	wait for MCLK_PERIOD; 
	incPut_tb <= '0'; 
	wait for MCLK_PERIOD;
        
        incPut_tb <= '1'; 
	wait for MCLK_PERIOD; 
	incPut_tb <= '0'; 
	wait for MCLK_PERIOD;
        
        incPut_tb <= '1'; 
	wait for MCLK_PERIOD; 
	incPut_tb <= '0'; 
	wait for MCLK_PERIOD;
        
        incPut_tb <= '1'; 
	wait for MCLK_PERIOD; 
	incPut_tb <= '0'; 
	wait for MCLK_PERIOD;
        
        incPut_tb <= '1'; 
	wait for MCLK_PERIOD; 
	incPut_tb <= '0'; 
	wait for MCLK_PERIOD;
        
        incPut_tb <= '1'; 
	wait for MCLK_PERIOD; 
	incPut_tb <= '0'; 
	wait for MCLK_PERIOD;
        
        incPut_tb <= '1'; 
	wait for MCLK_PERIOD; 
	incPut_tb <= '0'; 
	wait for MCLK_PERIOD;
        
        incPut_tb <= '1'; 
	wait for MCLK_PERIOD; 
	incPut_tb <= '0'; 
	wait for MCLK_PERIOD;
        
        incPut_tb <= '1'; 
	wait for MCLK_PERIOD; 
	incPut_tb <= '0'; 
	wait for MCLK_PERIOD;

        wait;
    end process;

end behavioral;
