Notes on how to use the configuration for Lyngsat Parser

1. org.andrew.tvheadend.parsers.lyngsat.ServiceParser_NorthernIreland_ExcludeChannel.txt
  o  Uses regular expressions to exclude a channel.
  o  If the channel matches any regular expression the channel is excluded.

2. org.andrew.tvheadend.parsers.lyngsat.ServiceParser_NorthernIreland_ParseChannel.txt
  o  Uses the following format [RegularExpression]|[Replacement]

    o  Example Channel Name: Sky 3D (09-01)
      o  We do not want (09-01) appearing in the channel name
      o  So we use the regular expression "\s[(].*[)]$"
        o  This will find the pattern
        o  We just want the information removed so there is no replacement text
        o  So our final line looks like this "\s[(].*[)]$|"
          o  Notice there is nothing to the right of the expression

3. org.andrew.tvheadend.parsers.lyngsat.ServiceParser_NorthernIreland_RemapChannel.txt
    o  This is the final step, it is used after all the the exclusions and
       renames have taken place.
    o  The goal of this step is to move the channel numbers given by Lyngsat
       to a different channel.  You may want to do this in some regions

