library ieee;
use ieee.std_logic_1164.all;

entity ShiftRegister_tb is
end ShiftRegister_tb;

architecture behavioral of ShiftRegister_tb is

  component ShiftRegister is
    port(
      data         : in std_logic;
      enableShift  : in std_logic;
      CLK          : in std_logic;
      D            : out std_logic_vector(4 downto 0)
    );
  end component;

  signal data_tb        : std_logic := '0';
  signal enableShift_tb : std_logic := '0';
  signal CLK_tb         : std_logic := '0';
  signal D_tb           : std_logic_vector(4 downto 0);

  constant MCLK_PERIOD : time := 20 ns;
  constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

begin

  UUT: ShiftRegister
    port map (
      data => data_tb,
      enableShift => enableShift_tb,
      CLK => CLK_tb,
      D => D_tb
    );

  clk_gen : process
  begin
      CLK_tb <= '0';
		wait for MCLK_HALF_PERIOD;
		CLK_tb <= '1';
		wait for MCLK_HALF_PERIOD;
  end process;

  stimulus : process
  begin
    wait for MCLK_PERIOD;

    data_tb <= '1';
    enableShift_tb <= '1';
    wait for MCLK_PERIOD;

    data_tb <= '0';
    wait for MCLK_PERIOD;

    data_tb <= '1';
    wait for MCLK_PERIOD;

    data_tb <= '1';
    wait for MCLK_PERIOD;

    data_tb <= '0';
    wait for MCLK_PERIOD;

    enableShift_tb <= '0';

    wait for MCLK_PERIOD * 2;

    wait;
  end process;

end behavioral;