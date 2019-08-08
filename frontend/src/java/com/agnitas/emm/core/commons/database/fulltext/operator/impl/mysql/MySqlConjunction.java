/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/

package com.agnitas.emm.core.commons.database.fulltext.operator.impl.mysql;

import java.util.List;
import java.util.stream.Collectors;

import com.agnitas.emm.core.commons.database.fulltext.operator.impl.BinaryOperator;

public class MySqlConjunction extends BinaryOperator {

    private static final String PLUS = "+";

    private static final String WHITESPACE = " ";

    private static final String DOUBLE_QUOTE = "\"";

    @Override
    public String process(List<String> operands) {
        checkOperands(operands);
        return operands.stream()
                .map(operand -> PLUS + DOUBLE_QUOTE + operand + DOUBLE_QUOTE)
                .collect(Collectors.joining(WHITESPACE));
    }

}